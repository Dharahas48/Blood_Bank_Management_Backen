package com.bloodbank.management.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DonorTest {

    @Test
    void testGettersAndSetters() {
        // Create a new Donor object using the all-args constructor
        Donor donor = new Donor(1L, "John Doe", "A+", "123-456-7890", "New York");

        // Verify that the getters return the expected values
        assertEquals(1L, donor.getId());
        assertEquals("John Doe", donor.getName());
        assertEquals("A+", donor.getBloodType());
        assertEquals("123-456-7890", donor.getContact());
        assertEquals("New York", donor.getCity());

        // Modify the Donor object using the setters
        donor.setId(2L);
        donor.setName("Jane Doe");
        donor.setBloodType("B+");
        donor.setContact("098-765-4321");
        donor.setCity("Los Angeles");

        // Verify that the getters return the updated values
        assertEquals(2L, donor.getId());
        assertEquals("Jane Doe", donor.getName());
        assertEquals("B+", donor.getBloodType());
        assertEquals("098-765-4321", donor.getContact());
        assertEquals("Los Angeles", donor.getCity());
    }

    @Test
    void testDefaultConstructor() {
        // Create a new Donor object using the default constructor
        Donor donor = new Donor();

        // Verify that the default values are null or 0
        assertNull(donor.getId());
        assertNull(donor.getName());
        assertNull(donor.getBloodType());
        assertNull(donor.getContact());
        assertNull(donor.getCity());
    }
}
