package com.ejemplo.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

  public static String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(12));
  }

  public static boolean checkPassword(String candidate, String hashed) {
    try {
      return BCrypt.checkpw(candidate.trim(), hashed.trim());
    } catch (Exception e) {
      return false;
    }
  }
}
