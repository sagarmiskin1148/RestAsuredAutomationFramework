package api.testcasses;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.endpoints.userEndPoints;
import api.payload.user;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class UserTestDD 
{
    @Test(priority=1, dataProvider="AllData", dataProviderClass=DataProviders.class)
    public void testCreateUser(String userId, String userName, String fname, String lname, String email, String pwd, String phone) 
    {
        // Creating user payload
        user userPayload = new user();

        // Debugging - Print received data
        System.out.println("Received userId: " + userId);

        // Validate userId before parsing
        if (userId != null && !userId.trim().isEmpty()) {
            try {
                userPayload.setId(Integer.parseInt(userId));
            } catch (NumberFormatException e) {
                System.out.println("Invalid userId format: " + userId);
                userPayload.setId(0); // Assign a default value
            }
        } else {
            System.out.println("Invalid userId: Empty or null value found!");
            userPayload.setId(0); // Assign a default value
        }

        // Setting other user details
        userPayload.setUsername(userName);
        userPayload.setFirstName(fname);
        userPayload.setLastName(lname);
        userPayload.setEmail(email); 
        userPayload.setPassword(pwd);
        userPayload.setPhone(phone);

        // Sending API request
        Response response = userEndPoints.createUser(userPayload);

        // Log response for debugging
        response.then().log().all();

        // Assertion for response validation
        Assert.assertEquals(response.getStatusCode(), 200);
    }
    
    
    @Test(priority=2, dataProvider="UserNamesData", dataProviderClass=DataProviders.class)
	public void testDeleteUser(String username)
	{
		
		Response response= userEndPoints.DeleteUser(username);
		
		System.out.println("Delete User Data.");
		
		//log response
		response.then().log().all();
		

		
		//validation
		Assert.assertEquals(response.getStatusCode(),200);
		
	}
}
