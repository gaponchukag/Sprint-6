package ru.sber.agadressbook

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.sber.agadressbook.models.Person

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddressBookWebControllerTest {

    @Autowired
    private lateinit var httpMock: MockMvc

    private val newRecord = Person(555, "Аристарх", "111-11-11", "переулок Сивцев Вражек")

    @Test
    fun viewRecordTest() {
        httpMock.perform(MockMvcRequestBuilders.get("/addressbook/1/view"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("view_record"))
    }

    @Test
    fun listRecordsTest() {
        httpMock.perform(MockMvcRequestBuilders.get("/addressbook/list"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list_records"))
    }

    @Test
    fun addRecordGetTest() {
        httpMock.perform(MockMvcRequestBuilders.get("/addressbook/add"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("add_record"))
    }

    @Test
    fun addRecordPostTest() {
        httpMock.perform(
            MockMvcRequestBuilders.post("/addressbook/add")
                .param("firstName", newRecord.firstName)
                .param("phoneNumber", newRecord.phoneNumber)
                .param("address",newRecord.address)
        ).andExpect(MockMvcResultMatchers.view().name("redirect:/addressbook/list"))

    }

    @Test
    fun editRecordTest() {
        httpMock.perform(
            MockMvcRequestBuilders.patch("/addressbook/1/edit")
                .param("id", "1")
                .param("firstName", newRecord.firstName)
                .param("phoneNumber", newRecord.phoneNumber)
                .param("address", newRecord.address)
        )
            .andExpect(MockMvcResultMatchers.view().name("redirect:/addressbook/list"))

        httpMock.perform(
            MockMvcRequestBuilders.get("/addressbook/list")
        )
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(newRecord.firstName)))
    }

    @Test
    fun deleteRecordSuccess() {
        httpMock.perform(MockMvcRequestBuilders.delete("/addressbook/1/delete"))
            .andExpect(MockMvcResultMatchers.view().name("redirect:/addressbook/list"))
    }

}