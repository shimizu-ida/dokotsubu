package dao;

import static org.junit.jupiter.api.Assertions.*; // Using JUnit Jupiter (JUnit 5)

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;


import model.Mutter;
import java.util.List;
import java.util.Random; // For generating sample byte arrays
// It's assumed that MutterDAO uses a properties file for DB connection.
// For real tests, this might need to be managed e.g. by pointing to a test DB
// or using an in-memory DB like H2, and pre-populating test data.

@Disabled("DAO tests require a database connection and setup, not available in this environment.")
class MutterDAOTest {

    private MutterDAO mutterDAO;
    private byte[] sampleImageData;

    @BeforeEach
    void setUp() throws Exception {
        mutterDAO = new MutterDAO(); // Assumes db.properties is set up for a test DB or is mocked
        
        // Create sample image data
        sampleImageData = new byte[1024]; // 1KB of dummy data
        new Random().nextBytes(sampleImageData);

        // TODO: Consider cleaning up or initializing the database table here.
        // For example, deleting all records from the 'mutter' table.
        // This requires a helper method to execute SQL directly.
    }

    @AfterEach
    void tearDown() throws Exception {
        // TODO: Consider cleaning up test data inserted during tests.
        // For example, deleting specific mutters created.
    }

    @Test
    void testCreateAndFindByIdWithImage() {
        // This test combines create and findById to verify image data persistence.
        System.out.println("Test: testCreateAndFindByIdWithImage - START");
        
        // 1. Create a Mutter object with image data
        String testUserName = "testUser_image";
        String testText = "This is a test mutter with an image.";
        int testUserId = 123; // Example user ID
        
        Mutter originalMutter = new Mutter(testUserName, testText, testUserId, sampleImageData);
        
        // 2. Save to database
        boolean createSuccess = mutterDAO.create(originalMutter);
        assertTrue(createSuccess, "Failed to create mutter in DAO.");
        System.out.println("Mutter created. Attempting to retrieve for verification.");

        // 3. Retrieve all mutters to find the one just created (since create doesn't return ID directly)
        //    A more robust way would be if create() returned the generated ID.
        //    Or, if a method like findByUserNameAndText existed.
        //    For this example, we assume the latest entry is ours if sorted by ID desc.
        List<Mutter> mutters = mutterDAO.findAll();
        assertNotNull(mutters, "findAll should return a list, not null.");
        assertFalse(mutters.isEmpty(), "Mutter list should not be empty after creation.");
        
        Mutter retrievedMutter = null;
        for(Mutter m : mutters){
            // This is a simplistic way to find the created mutter.
            // It assumes no other identical posts are made concurrently by the same user.
            if(m.getUserName().equals(testUserName) && m.getText().equals(testText) && m.getUserId() == testUserId){
                retrievedMutter = m;
                break;
            }
        }
        assertNotNull(retrievedMutter, "Could not retrieve the created mutter for verification.");
        System.out.println("Retrieved Mutter ID: " + retrievedMutter.getId());


        // 4. Verify the retrieved Mutter's image data
        //    We use the ID from the retrievedMutter to do a specific findByIdAndUserId call.
        Mutter foundByIdMutter = mutterDAO.findByIdAndUserId(retrievedMutter.getId(), testUserId);
        assertNotNull(foundByIdMutter, "findByIdAndUserId should find the mutter.");
        assertNotNull(foundByIdMutter.getImageData(), "Retrieved mutter should have image data.");
        assertArrayEquals(sampleImageData, foundByIdMutter.getImageData(), "Image data should match the original.");
        assertEquals(testText, foundByIdMutter.getText(), "Text should match.");
        
        System.out.println("Test: testCreateAndFindByIdWithImage - PASSED (structurally)");
        // TODO: Add cleanup for the created mutter entry if needed.
    }

    @Test
    void testFindAllWithImages() {
        System.out.println("Test: testFindAllWithImages - START");
        // This test assumes some data with images already exists, or was created by other tests/setup.
        // For a more isolated test, create a known set of Mutters here.
        
        // Create a couple of mutters, one with image, one without, to test findAll
        Mutter mutterWithImage = new Mutter("user_img_all", "Text for image mutter (findAll)", 1, sampleImageData);
        Mutter mutterWithoutImage = new Mutter("user_noimg_all", "Text for no-image mutter (findAll)", 2, null);
        
        assertTrue(mutterDAO.create(mutterWithImage), "Failed to create mutterWithImage for findAll test.");
        assertTrue(mutterDAO.create(mutterWithoutImage), "Failed to create mutterWithoutImage for findAll test.");

        List<Mutter> mutters = mutterDAO.findAll();
        assertNotNull(mutters, "findAll should return a list.");
        assertTrue(mutters.size() >= 2, "findAll should return at least the two mutters created for this test.");

        boolean foundMutterWithImage = false;
        boolean foundMutterWithoutImage = false;

        for (Mutter m : mutters) {
            if (m.getText().equals("Text for image mutter (findAll)")) {
                foundMutterWithImage = true;
                assertNotNull(m.getImageData(), "Mutter with image should have non-null image data in findAll.");
                assertArrayEquals(sampleImageData, m.getImageData(), "Image data for mutterWithImage in findAll should match.");
            } else if (m.getText().equals("Text for no-image mutter (findAll)")) {
                foundMutterWithoutImage = true;
                assertNull(m.getImageData(), "Mutter without image should have null image data in findAll.");
            }
        }
        
        assertTrue(foundMutterWithImage, "Mutter with image was not found in findAll results.");
        assertTrue(foundMutterWithoutImage, "Mutter without image was not found in findAll results.");
        System.out.println("Test: testFindAllWithImages - PASSED (structurally)");
        // TODO: Cleanup created mutters.
    }
    
    @Test
    void testCreateMutterWithoutImage() {
        System.out.println("Test: testCreateMutterWithoutImage - START");
        String userName = "noImageUser";
        String text = "This mutter has no image.";
        int userId = 789;
        Mutter mutter = new Mutter(userName, text, userId, null); // Explicitly null image data

        boolean success = mutterDAO.create(mutter);
        assertTrue(success, "Creating mutter without image should succeed.");

        // Retrieve and verify (similar logic to testCreateAndFindByIdWithImage)
        List<Mutter> mutters = mutterDAO.findAll();
        Mutter retrievedMutter = null;
        for(Mutter m : mutters){
            if(m.getUserName().equals(userName) && m.getText().equals(text) && m.getUserId() == userId){
                retrievedMutter = m;
                break;
            }
        }
        assertNotNull(retrievedMutter, "Could not retrieve the created mutter (without image).");
        Mutter foundByIdMutter = mutterDAO.findByIdAndUserId(retrievedMutter.getId(), userId);
        assertNotNull(foundByIdMutter, "findByIdAndUserId should find the mutter (without image).");
        assertNull(foundByIdMutter.getImageData(), "Image data should be null for mutter created without an image.");
        assertEquals(text, foundByIdMutter.getText());
        System.out.println("Test: testCreateMutterWithoutImage - PASSED (structurally)");
        // TODO: Cleanup.
    }
}
