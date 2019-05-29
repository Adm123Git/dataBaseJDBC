package interfaces;

import annotation.Overload;
import com.sun.istack.internal.Nullable;
import domain.ApplicationUser;

public interface UserService {

    public enum Result {INPUT_DATA_ERROR, SUCCESS, ERROR}

    public Result addUser(String login, String password);

    @Overload
    public Result updUser(String login, String newLogin, String newPassword);

    @Overload
    public Result updUser(int id, String newLogin, String newPassword);

    @Overload
    public Result delUser(int id);

    @Overload
    public Result delUser(String login);

    @Nullable
    @Overload
    public ApplicationUser getUser(int id);

    @Nullable
    @Overload
    public ApplicationUser getUser(String login);

    @Nullable
    @Overload
    public ApplicationUser getUser(String login, String password);

    public void printUserList();


}
