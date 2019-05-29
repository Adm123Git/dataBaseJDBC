package entities;

public class ApplicationUser {

    private Integer id;
    private String userLogin;
    private String password;

    public static class Builder {
        private Integer id = null;
        private String userLogin = "";
        private String password = "";

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder login(String userLogin) {
            this.userLogin = userLogin;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public ApplicationUser build() {
            ApplicationUser user = new ApplicationUser();
            user.id = id;
            user.userLogin = userLogin;
            user.password = password;
            return user;
        }
    }

    private ApplicationUser(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{id: " + id + " | login: " + userLogin + " | password: " + password + "}";
    }
}
