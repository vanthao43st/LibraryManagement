package org.uet.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.uet.enums.Gender;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student {
    String id;
    String name;
    Gender gender;
    LocalDate birthDate;
    String className;
    String address;
    String major;
    String phoneNumber;
    String email;
}
