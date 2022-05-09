package models;

public class GenerateTokenResponse {
    /*{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6IkFsZXgiLCJwYXNzd29yZCI6ImFzZHNhZCNmcmV3X0RGUzIiLCJpYXQiOjE2NTAyNzExNTl9.ap1f8pWSdvCnDwsL9KGpkHU3xXRv-65lUOgt78bHCII",
    "expires": "2022-04-25T08:39:19.126Z",
    "status": "Success",
    "result": "User authorized successfully."
}*/

    private String token;
    private String expires;
    private String status;
    private String result;

    public String getToken() {
        return token;
    }

    public String getExpires() {
        return expires;
    }

    public String getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }

}
