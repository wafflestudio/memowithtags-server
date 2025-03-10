package com.wafflestudio.toyproject.memoWithTags

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

data class Tag(
    val name: String,
    val colorHex: String
)

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TagIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper()

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
    }

    @Test
    fun `create tag`() {
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

        mockMvc.perform(
            post("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTagRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("testTag"))
            .andExpect(jsonPath("$.colorHex").value("#000000"))
    }

    @Test
    fun `get tag`() {
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

        mockMvc.perform(
            post("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTagRequest))
        )
            .andExpect(status().isCreated)

        val createTagRequest1 = mapOf(
            "name" to "testTag1",
            "colorHex" to "#000001"
        )

        mockMvc.perform(
            post("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTagRequest1))
        )
            .andExpect(status().isCreated)

        val expectedTags = listOf(
            Tag("testTag", "#000000"),
            Tag("testTag1", "#000001")
        )

        val expectedJson = mapper.writeValueAsString(expectedTags)

        mockMvc.perform(
            get("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedJson, false))
    }

    @Test
    fun `update tag`() {
        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "password"
        )

        val mvcResult: MvcResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andReturn()

        val responseBody = mvcResult.response.contentAsString
        val jsonNode = mapper.readTree(responseBody)
        val accessToken = jsonNode.get("accessToken").asText()

        val createTagRequest = mapOf(
            "name" to "testTag",
            "colorHex" to "#000000"
        )

        val createResult = mockMvc.perform(
            post("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTagRequest))
        ).andReturn()

        val createdTagResponse = createResult.response.contentAsString
        val createdTag = mapper.readTree(createdTagResponse)
        val tagId = createdTag.get("id").asInt()

        val updateTagRequest = mapOf(
            "name" to "updateTag",
            "colorHex" to "#000001"
        )

        mockMvc.perform(
            put("/api/v1/tag/$tagId")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateTagRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("updateTag"))
            .andExpect(jsonPath("$.colorHex").value("#000001"))

        val expectedTags = listOf(
            Tag("updateTag", "#000001")
        )

        val expectedJson = mapper.writeValueAsString(expectedTags)

        mockMvc.perform(
            get("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedJson, false))
    }

    @Test
    fun `delete tag`() {
        val loginRequest = mapOf(
            "email" to "test@example.com",
            "password" to "password"
        )

        val mvcResult: MvcResult = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest))
        )
            .andReturn()

        val responseBody = mvcResult.response.contentAsString
        val jsonNode = mapper.readTree(responseBody)
        val accessToken = jsonNode.get("accessToken").asText()

        val createTagRequest = mapOf(
            "name" to "testTag",
            "colorHex" to "#000000"
        )

        mockMvc.perform(
            post("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTagRequest))
        )

        val createTagRequest1 = mapOf(
            "name" to "testTag1",
            "colorHex" to "#000001"
        )

        mockMvc.perform(
            post("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createTagRequest1))
        )

        val expectedTags = listOf(
            Tag("testTag", "#000000"),
            Tag("testTag1", "#000001")
        )

        val expectedJson = mapper.writeValueAsString(expectedTags)

        val tagResponse = mockMvc.perform(
            get("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedJson, false))
            .andReturn()

        val tags = tagResponse.response.contentAsString

        val tagId = (JsonPath.parse(tags).read<List<Map<String, Any>>>("$[?(@.name=='testTag')]")).firstOrNull()?.get("id")

        mockMvc.perform(
            delete("/api/v1/tag/$tagId")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isNoContent)

        val expectedTag = listOf(
            Tag("testTag1", "#000001")
        )

        val expectedJson1 = mapper.writeValueAsString(expectedTag)

        mockMvc.perform(
            get("/api/v1/tag")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isOk)
            .andExpect(content().json(expectedJson1, false))
    }
}
