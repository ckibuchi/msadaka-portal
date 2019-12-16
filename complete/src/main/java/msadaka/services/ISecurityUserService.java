package msadaka.services;


public interface ISecurityUserService {

    String validatePasswordResetToken(long id, String token);

}