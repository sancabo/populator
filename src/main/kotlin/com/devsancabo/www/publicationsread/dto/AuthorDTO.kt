package com.devsancabo.www.publicationsread.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

@Validated
data class AuthorDTO(var id: Long?, var username: @NotNull @NotBlank @NotEmpty String) {

    fun withUsername(username: String): AuthorDTO {
        this.username = username
        return this
    }

    fun withId(id: Long?): AuthorDTO {
        this.id = id
        return this
    }
}