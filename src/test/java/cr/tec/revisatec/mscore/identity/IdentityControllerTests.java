package cr.tec.revisatec.mscore.identity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IdentityControllerTests {

    private static final Pattern ID = Pattern.compile("\"id\"\\s*:\\s*(\\d+)");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void professorSeesOnlyAssignedCourses() throws Exception {
        long assigned = createCourse("Curso Profesor A");
        createCourse("Curso Ajeno");

        mockMvc.perform(post("/professors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Marta Rojas\",\"email\":\"Marta.Rojas@itcr.ac.cr\",\"courseIds\":[" + assigned + "]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("marta.rojas@itcr.ac.cr"));

        mockMvc.perform(get("/me").param("email", "marta.rojas@itcr.ac.cr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("professor"))
                .andExpect(jsonPath("$.name").value("Marta Rojas"))
                .andExpect(jsonPath("$.courses", hasSize(1)))
                .andExpect(jsonPath("$.courses[0].id").value(assigned))
                .andExpect(jsonPath("$.memberships", hasSize(0)));
    }

    @Test
    void studentSeesMembershipsAndHeaderWinsOverParam() throws Exception {
        long courseId = createCourse("Curso Estudiante");
        long groupId = createGroup("Grupo Estudiante", courseId);
        mockMvc.perform(post("/groups/" + groupId + "/members").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Pablo Vargas\",\"email\":\"pablo.vargas@estudiantec.cr\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/me")
                        .header("X-User-Email", "pablo.vargas@estudiantec.cr")
                        .param("email", "otro@estudiantec.cr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("student"))
                .andExpect(jsonPath("$.name").value("Pablo Vargas"))
                .andExpect(jsonPath("$.courses[0].id").value(courseId))
                .andExpect(jsonPath("$.memberships[0].group.id").value(groupId))
                .andExpect(jsonPath("$.memberships[0].course.id").value(courseId));
    }

    @Test
    void unknownEmailAndMissingEmail() throws Exception {
        mockMvc.perform(get("/me").param("email", "nadie@estudiantec.cr"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("El correo nadie@estudiantec.cr no está registrado en RevisaTEC"));

        mockMvc.perform(get("/me"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("email es obligatorio"));
    }

    @Test
    void professorValidationsAndDelete() throws Exception {
        long courseId = createCourse("Curso Validacion Profesor");
        String body = "{\"name\":\"Luis Mora\",\"email\":\"luis.mora@itcr.ac.cr\",\"courseIds\":[" + courseId + "]}";

        long professorId = extractId(mockMvc.perform(post("/professors")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andReturn());
        mockMvc.perform(post("/professors").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
        mockMvc.perform(post("/professors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sin curso\",\"email\":\"sin.curso@itcr.ac.cr\",\"courseIds\":[999999]}"))
                .andExpect(status().isNotFound());
        mockMvc.perform(post("/professors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Vacio\",\"email\":\"vacio@itcr.ac.cr\",\"courseIds\":[]}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/professors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[?(@.email == 'luis.mora@itcr.ac.cr')]").exists());

        mockMvc.perform(delete("/professors/" + professorId)).andExpect(status().isOk());
        mockMvc.perform(delete("/professors/" + professorId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/me").param("email", "luis.mora@itcr.ac.cr")).andExpect(status().isNotFound());
    }

    private long createCourse(String name) throws Exception {
        return extractId(mockMvc.perform(post("/courses").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\"}"))
                .andExpect(status().isCreated()).andReturn());
    }

    private long createGroup(String name, long courseId) throws Exception {
        return extractId(mockMvc.perform(post("/groups").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\",\"repoUrl\":\"https://github.com/revisatec/demo\","
                                + "\"courseId\":" + courseId + "}"))
                .andExpect(status().isCreated()).andReturn());
    }

    private static long extractId(MvcResult result) throws Exception {
        Matcher matcher = ID.matcher(result.getResponse().getContentAsString());
        if (!matcher.find()) {
            throw new IllegalStateException("La respuesta no contiene id");
        }
        return Long.parseLong(matcher.group(1));
    }
}
