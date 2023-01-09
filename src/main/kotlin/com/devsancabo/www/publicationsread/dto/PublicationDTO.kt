package com.devsancabo.www.publicationsread.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import java.time.LocalDateTime

@Validated
data class PublicationDTO(val id: String?,
                          var content: @NotNull @NotBlank @NotEmpty String,
                          val datetime: LocalDateTime,
                          val author: AuthorDTO) {
    override fun toString(): String {
        return StringBuilder()
            .append("[content=").append(content)
            .append(", datetime=").append(datetime)
            .append(", authorName=").append(author.username)
            .append("]").toString()
    }

    data class Builder(var id: String? = null,
                       var content: @NotNull @NotBlank @NotEmpty String? = null,
                       var datetime: LocalDateTime? = null,
                       var author: AuthorDTO? = null) {

        fun id(id: String?) = apply {this.id = id}
        fun content(content: @NotNull @NotBlank @NotEmpty String) = apply {this.content = content}
        fun datetime(datetime: LocalDateTime) = apply {this.datetime = datetime}
        fun author(author: AuthorDTO) = apply {this.author = author}

        fun build() = PublicationDTO(id,content!!, datetime!!, author!!)
    }

    companion object {
        fun builder(): Builder {return Builder()}
    }
}