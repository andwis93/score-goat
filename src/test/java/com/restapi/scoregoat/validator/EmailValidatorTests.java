package com.restapi.scoregoat.validator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmailValidatorTests {
   private final EmailValidator validator = new EmailValidator();

   @Test
   void testEmailValidator() {
      //Given
      String email1 = "test@test.com";
      String email2 = "test@ffdv";

      //When
      boolean verification1 = validator.emailValidator(email1);
      boolean verification2 = validator.emailValidator(email2);

      //Then
      assertTrue(verification1);
      assertFalse(verification2);
   }
}
