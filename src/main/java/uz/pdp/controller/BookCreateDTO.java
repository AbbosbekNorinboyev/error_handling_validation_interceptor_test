package uz.pdp.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookCreateDTO {
    @NotBlank(message = "title.notnull")
    String title;
    @NotBlank(message = "Author can not be null")
    String author;
}
