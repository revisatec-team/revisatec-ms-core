package cr.tec.revisatec.mscore.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GroupMemberControllerTests {

    private static final Pattern ID = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addListFindAndRemoveMember() throws Exception {
        long courseId = createCourse("Curso Integrantes");
        long groupId = createGroup("Grupo Integrantes", courseId);

        long memberId = extractId(mockMvc.perform(post("/groups/" + groupId + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Ana Mora","email":"Ana.Mora@Estudiantec.cr","githubUser":"anamora"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("ana.mora@estudiantec.cr"))
                .andExpect(jsonPath("$.groupId").value(groupId))
                .andReturn());

        mockMvc.perform(get("/groups/" + groupId + "/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRecords").value(1))
                .andExpect(jsonPath("$.members[0].name").value("Ana Mora"));

        mockMvc.perform(get("/members/me")
                        .param("email", "ana.mora@estudiantec.cr")
                        .param("courseId", String.valueOf(courseId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.member.id").value(memberId))
                .andExpect(jsonPath("$.group.id").value(groupId));

        mockMvc.perform(delete("/groups/" + groupId + "/members/" + memberId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/members/me")
                        .param("email", "ana.mora@estudiantec.cr")
                        .param("courseId", String.valueOf(courseId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void rejectsDuplicateEmailInSameCourse() throws Exception {
        long courseId = createCourse("Curso Duplicados");
        long firstGroup = createGroup("Grupo A", courseId);
        long secondGroup = createGroup("Grupo B", courseId);
        String body = """
                {"name":"Luis Rojas","email":"luis.rojas@estudiantec.cr"}
                """;

        mockMvc.perform(post("/groups/" + firstGroup + "/members")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/groups/" + secondGroup + "/members")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void validatesInputAndMissingResources() throws Exception {
        long courseId = createCourse("Curso Validaciones");
        long groupId = createGroup("Grupo Validaciones", courseId);

        mockMvc.perform(post("/groups/" + groupId + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sin correo\",\"email\":\"no-es-correo\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        mockMvc.perform(get("/groups/999999/members"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/groups/" + groupId + "/members/999999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/members/me").param("courseId", String.valueOf(courseId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("email es obligatorio"));
    }

    @Test
    void deletingGroupRemovesItsMembers() throws Exception {
        long courseId = createCourse("Curso Borrado");
        long groupId = createGroup("Grupo Borrado", courseId);
        mockMvc.perform(post("/groups/" + groupId + "/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Eva Solano\",\"email\":\"eva.solano@estudiantec.cr\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/groups/" + groupId)).andExpect(status().isOk());

        mockMvc.perform(get("/members/me")
                        .param("email", "eva.solano@estudiantec.cr")
                        .param("courseId", String.valueOf(courseId)))
                .andExpect(status().isNotFound());
    }

    private long createCourse(String name) throws Exception {
        return extractId(mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(coursePayload(name)))
                .andExpect(status().isCreated())
                .andReturn());
    }

    private long createGroup(String name, long courseId) throws Exception {
        return extractId(mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\",\"repoUrl\":\"https://github.com/revisatec/demo\","
                                + "\"courseId\":" + courseId + "}"))
                .andExpect(status().isCreated())
                .andReturn());
    }

    private static String coursePayload(String name) {
        return "{\"name\":\"" + name + "\"}";
    }

    private static long extractId(MvcResult result) throws Exception {
        Matcher matcher = ID.matcher(result.getResponse().getContentAsString());
        if (!matcher.find()) {
            throw new IllegalStateException("La respuesta no contiene id");
        }
        return Long.parseLong(matcher.group(1));
    }
}
