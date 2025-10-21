//package com.laundry.freshfoldlaundryapp.util;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class PhoneNumberValidatorTest {
//
//    @Test
//    void testValidMobileNumbers() {
//        // Valid mobile numbers
//        assertTrue(PhoneNumberValidator.isValidPhoneNumber("0771234567"));
//        assertTrue(PhoneNumberValidator.isValidPhoneNumber("0781234567"));
//        assertTrue(PhoneNumberValidator.isValidPhoneNumber("0751234567"));
//        assertTrue(PhoneNumberValidator.isValidPhoneNumber("0701234567"));
//    }
//
//    @Test
//    void testValidLandlineNumbers() {
//        // Valid landline numbers
//        assertTrue(PhoneNumberValidator.isValidPhoneNumber("0111234567"));
//        assertTrue(PhoneNumberValidator.isValidPhoneNumber("0112345678"));
//    }
//
//    @Test
//    void testInvalidPhoneNumbers() {
//        // Invalid formats
//        assertFalse(PhoneNumberValidator.isValidPhoneNumber("081234567")); // Wrong prefix
//        assertFalse(PhoneNumberValidator.isValidPhoneNumber("077123456")); // Too short
//        assertFalse(PhoneNumberValidator.isValidPhoneNumber("07712345678")); // Too long
//        assertFalse(PhoneNumberValidator.isValidPhoneNumber("01112345")); // Too short landline
//        assertFalse(PhoneNumberValidator.isValidPhoneNumber("011123456789")); // Too long landline
//        assertFalse(PhoneNumberValidator.isValidPhoneNumber("abc1234567")); // Contains letters
//        assertFalse(PhoneNumberValidator.isValidPhoneNumber(""));
//        assertFalse(PhoneNumberValidator.isValidPhoneNumber(null));
//    }
//
//    @Test
//    void testPhoneNumberWithSpaces() {
//        // Test with spaces and formatting
//        assertTrue(PhoneNumberValidator.isValidPhoneNumber("077 123 4567"));
//        assertTrue(PhoneNumberValidator.isValidPhoneNumber("011 123 4567"));
//    }
//
//    @Test
//    void testPhoneNumberTypes() {
//        assertEquals("MOBILE", PhoneNumberValidator.getPhoneNumberType("0771234567"));
//        assertEquals("LANDLINE", PhoneNumberValidator.getPhoneNumberType("0111234567"));
//        assertEquals("INVALID", PhoneNumberValidator.getPhoneNumberType("081234567"));
//    }
//
//    @Test
//    void testPhoneNumberFormatting() {
//        assertEquals("077 123 4567", PhoneNumberValidator.formatPhoneNumber("0771234567"));
//        assertEquals("011 123 4567", PhoneNumberValidator.formatPhoneNumber("0111234567"));
//    }
//}
