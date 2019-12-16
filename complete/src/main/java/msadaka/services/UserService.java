package msadaka.services;

import msadaka.models.User;

public interface UserService {
    public User findUserByEmail(String email);
    public User findUserByUsername(String email);
    public User findByEmailOrUsername(String email,String username);
    public void saveUser(User user);
    public void deleteUser(User user);
    public void createPasswordResetTokenForUser(User user, String token);
    public void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);
}