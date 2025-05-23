package com.wafflestudio.toyproject.memoWithTags

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

data class Memo(
    val content: String,
    val tagIds: List<Long>,
    val locked: Boolean
)

@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureMockMvc
@Transactional
class MemoIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper()

    fun createMemoJson(memoNumber: Int, tagIds: List<Long>): String {
        val payload = mapOf(
            "content" to "test content$memoNumber",
            "tagIds" to tagIds
        )
        return mapper.writeValueAsString(payload)
    }

    fun performCreateMemoRequest(memoNumber: Int, tagIds: List<Long>, accessToken: String): ResultActions {
        val jsonPayload = createMemoJson(memoNumber, tagIds)
        return mockMvc.perform(
            post("api/v1/memo")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload)
        )
    }

    @BeforeEach
    fun setup() {
        val mailRequest = mapOf(
            "email" to "test@example.com"
        )

        mockMvc.perform(
            post("/api/v1/auth/send-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(mailRequest))
        )
            .andExpect(status().isOk)

        val verifyRequest = mapOf(
            "email" to "test@example.com",
            "verificationCode" to "000000"
        )

        mockMvc.perform(
            post("/api/v1/auth/verify-email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(verifyRequest))
        )
            .andExpect(status().isOk)

        val requestBody = mapOf(
            "email" to "test@example.com",
            "nickname" to "John Doe",
            "password" to "password"
        )

        val requestJson = mapper.writeValueAsString(requestBody)

        mockMvc.perform(
            post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())

        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "password"
        )

        val mvcResult: MvcResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
            .andReturn()

        val responseBody = mvcResult.response.contentAsString
        val jsonNode = mapper.readTree(responseBody)
        val accessToken = jsonNode.get("accessToken").asText()

        val createTagRequest = mapOf(
            "name" to "testTag",
            "colorHex" to "#000000"
        )

        val createTagRequest1 = mapOf(
            "name" to "testTag1",
            "colorHex" to "#000001"
        )

        mockMvc.perform(
            post("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTagRequest))
        )

        mockMvc.perform(
            post("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTagRequest1))
        )
    }

    @Test
    fun `create memo`() {
        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "password"
        )

        val mvcResult: MvcResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
            .andReturn()

        val responseBody = mvcResult.response.contentAsString
        val jsonNode = mapper.readTree(responseBody)
        val accessToken = jsonNode.get("accessToken").asText()

        val createMemoRequest = mapOf(
            "content" to "test content",
            "tagIds" to listOf(1L, 2L),
            "locked" to false
        )

        mockMvc.perform(
            post("/api/v1/memo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createMemoRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.content").value("test content"))
            .andExpect(jsonPath("$.tagIds").isArray) // 배열 여부 확인
            .andExpect(jsonPath("$.tagIds[0]").value(1))
            .andExpect(jsonPath("$.tagIds[1]").value(2))
            .andExpect(jsonPath("$.locked").value(false))
    }

    @Test
    fun `search memo`() {
        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "password"
        )

        val mvcResult: MvcResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
            .andReturn()

        val responseBody = mvcResult.response.contentAsString
        val jsonNode = mapper.readTree(responseBody)
        val accessToken = jsonNode.get("accessToken").asText()

        for (i in 1..27) {
            performCreateMemoRequest(i, listOf(1L, 2L), accessToken)
        }

        mockMvc.perform(
            get("/search-memo")
                .header("Authorization", "Bearer $accessToken")
                .param("content", "test content")
                .param("tagIds", "1,2")
                .param("startDate", "2024-01-01T00:00:00Z")
                .param("endDate", "2025-12-31T23:59:59Z")
                .param("page", "1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.page").value(1))
            .andExpect(jsonPath("$.totalPages").value(3))
            .andExpect(jsonPath("$.totalResults").value(27))
            .andExpect(jsonPath("$.results[0].content").value("test content30"))
            .andExpect(jsonPath("$.results[1].content").value("test content29"))
            .andExpect(jsonPath("$.results[0].tagIds").value(listOf(1L, 2L)))
            .andExpect(jsonPath("$.results[1].tagIds").value(listOf(1L, 2L)))

        mockMvc.perform(
            get("/search-memo")
                .header("Authorization", "Bearer $accessToken")
                .param("content", "test content1")
                .param("tagIds", "1")
                .param("startDate", "2024-01-01T00:00:00Z")
                .param("endDate", "2025-12-31T23:59:59Z")
                .param("page", "1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.page").value(1))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.totalResults").value(1))
            .andExpect(jsonPath("$.results[0].content").value("test content1"))
            .andExpect(jsonPath("$.results[0].tagIds").value(listOf(1L, 2L)))
            .andExpect(jsonPath("$.results[0].locked").value(false))
    }

    @Test
    fun `delete memo`() {
        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "password"
        )

        val mvcResult: MvcResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
            .andReturn()

        val responseBody = mvcResult.response.contentAsString
        val jsonNode = mapper.readTree(responseBody)
        val accessToken = jsonNode.get("accessToken").asText()

        for (i in 1..11) {
            performCreateMemoRequest(i, listOf(1L, 2L), accessToken)
        }

        // 메모 삭제
        val searchResult = mockMvc.perform(
            get("/search-memo")
                .header("Authorization", "Bearer $accessToken")
                .param("content", "test content1")
                .param("tagIds", "1")
                .param("startDate", "2024-01-01T00:00:00Z")
                .param("endDate", "2025-12-31T23:59:59Z")
                .param("page", "1")
        )
            .andExpect(status().isOk)
            .andReturn()

        val content = searchResult.response.contentAsString
        val idList: List<Int> = JsonPath.parse(content).read("$.results[*].id")

        assert(idList.size == 1)
        val id = idList[0]

        mockMvc.perform(
            delete("api/vi/memo/$id")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isNoContent)

        // 삭제 잘 되었는지 확인, page 개수 1
        mockMvc.perform(
            get("/search-memo")
                .header("Authorization", "Bearer $accessToken")
                .param("content", "test content")
                .param("tagIds", "1")
                .param("startDate", "2024-01-01T00:00:00Z")
                .param("endDate", "2025-12-31T23:59:59Z")
                .param("page", "1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.page").value(1))
            .andExpect(jsonPath("$.totalPages").value(1))
            .andExpect(jsonPath("$.totalResults").value(10))
            .andExpect(jsonPath("$.results[0].content").value("test content11"))
            .andExpect(jsonPath("$.results[0].tagIds").value(listOf(1L, 2L)))
            .andExpect(jsonPath("$.results[0].locked").value(false))
    }

    @Test
    fun `update memo`() {
        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "password"
        )

        val mvcResult: MvcResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists())
            .andReturn()

        val responseBody = mvcResult.response.contentAsString
        val jsonNode = mapper.readTree(responseBody)
        val accessToken: String = jsonNode.get("accessToken").asText()

        performCreateMemoRequest(1, listOf(1L, 2L), accessToken)

        val searchResult = mockMvc.perform(
            get("/search-memo")
                .header("Authorization", "Bearer $accessToken")
                .param("content", "test content1")
                .param("tagIds", "1")
                .param("startDate", "2024-01-01T00:00:00Z")
                .param("endDate", "2025-12-31T23:59:59Z")
                .param("page", "1")
        )
            .andExpect(status().isOk)
            .andReturn()

        val content = searchResult.response.contentAsString
        val idList: List<Int> = JsonPath.parse(content).read("$.results[*].id")

        assert(idList.size == 1)
        val id = idList[0]

        val updateMemoRequest = mapOf(
            "content" to "test content11",
            "tagIds" to "1,2",
            "locked" to false
        )

        // 메모 업데이트
        mockMvc.perform(
            put("/api/v1/memo/$id")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateMemoRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().json(mapper.writeValueAsString(updateMemoRequest), false))

        mockMvc.perform(
            get("/search-memo")
                .header("Authorization", "Bearer $accessToken")
                .param("content", "test content11")
                .param("tagIds", "1")
                .param("startDate", "2024-01-01T00:00:00Z")
                .param("endDate", "2025-12-31T23:59:59Z")
                .param("page", "1")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.results[0].content").value("test content11"))
    }
}
