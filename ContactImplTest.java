
import static org.junit.Assert.*;
import org.junit.*;



public class ContactImplTest {
	private ContactImpl c = null;
	private ContactImpl d = null;
	
	
	
	@Before
	public void setUp() throws Exception{
		c = new ContactImpl(1, "Fred", "captain cool");
		d = new ContactImpl(0, "Boris", "James Bond");
	}
	
	@After
	public void tearDown() throws Exception{
		c = null;
		d = null;
	}


	@Test
	public void testGetId() {
		
		int num = c.getId();
		assertEquals(1,num);
	}

	@Test
	public void testGetName() {
		String name = c.getName();
		assertEquals("Fred",name);
	}

	@Test
	public void testGetNotes() {
		String notes = c.getNotes();
		assertEquals("captain cool", notes);
	}

	@Test
	public void testAddMeetings() {
		
	}

	@Test
	public void testGetMeetings() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNotes() {
		fail("Not yet implemented");
	}

}
