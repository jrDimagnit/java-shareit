package ru.practicum.shareit.user;

import lombok.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long id;
    @NotBlank(groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    private String email;

}
