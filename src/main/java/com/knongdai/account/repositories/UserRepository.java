package com.knongdai.account.repositories;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.knongdai.account.entities.Role;
import com.knongdai.account.entities.User;
import com.knongdai.account.entities.UserRegister;
import com.knongdai.account.entities.forms.FrmSocailUser;
import com.knongdai.account.entities.forms.FrmUpdateUser;
import com.knongdai.account.entities.forms.FrmUserRegister;
import com.knongdai.account.entities.forms.UserInfo;
import com.knongdai.account.entities.forms.UserLogin;
import com.knongdai.account.entities.forms.UserMobileLogin;
import com.knongdai.account.repositories.sql.UserSQL;

@Repository
public interface UserRepository {
	
	@Select(UserSQL.R_USER_BY_EMAIL)
	@Results(value={
			// new field store status such as '0': Inactive, '1': Active, '2': Deleted, '3': Locked (Ean Sokchomrern, 15/09/2016)
			@Result(property="status" , column="status"),    
			// new field store verification_code ((Ean Sokchomrern, 15/09/2016)
			@Result(property="verification_code" , column="verification_code"),
			
			@Result(property="userId" , column="userid"),
			@Result(property="email" , column="email"),
			@Result(property="password" , column="password"),
			@Result(property="username" , column="username"),
			@Result(property="gender" , column="gender"),
			@Result(property="registeredDate" , column="registerdate"),
			@Result(property="userImageUrl" , column="userimageurl"),
			@Result(property="point" , column="point"),
			@Result(property="universityId" , column="universityid"),
			@Result(property="departmentId" , column="departmentid"),
			@Result(property="userStatus" , column="userstatus"),
			@Result(property="socialId" , column="sc_fb_id"),
			@Result(property="socialType" , column="sc_type"),
			@Result(property="isConfirmed" , column="isconfirmed"),
			@Result(property="signUpWith" , column="signup_with"),
			@Result(property="encUserId" , column="userid"),
			@Result(property="userHash" , column="user_hash"),
			@Result(property="status" , column="status"),
			@Result(property="roles" , column="userid" ,
					many = @Many(select = "findRolesByUserId")
			)
	})
	User findUserByEmail(UserLogin userLogin);

	@Select(UserSQL.R_ROLES_BY_USER_ID)
	@Results(value={
			@Result(property="roleId" , column="role_id"),
			@Result(property="roleName" , column="role_name")
	})
	List<Role> findRolesByUserId(@Param("userid") int userid);
	
	@Select(UserSQL.R_USER_BY_USER_ID)
	@Results(value={
			@Result(property="userId" , column="userid"),
			@Result(property="email" , column="email"),
			@Result(property="password" , column="password"),
			@Result(property="username" , column="username"),
			@Result(property="gender" , column="gender"),
			@Result(property="registeredDate" , column="registerdate"),
			@Result(property="userImageUrl" , column="userimageurl"),
			@Result(property="point" , column="point"),
			@Result(property="universityId" , column="universityid"),
			@Result(property="departmentId" , column="departmentid"),
			@Result(property="userStatus" , column="userstatus"),
			@Result(property="socialId" , column="sc_fb_id"),
			@Result(property="socialType" , column="sc_type"),
			@Result(property="isConfirmed" , column="isconfirmed"),
			@Result(property="signUpWith" , column="signup_with"),
			@Result(property="encUserId" , column="userid"),
			@Result(property="userHash" , column="user_hash"),
			@Result(property="status" , column="status"),
			@Result(property="roles" , column="userid" ,
					many = @Many(select = "findRolesByUserId")
			)
	})
	User findUserByUserId(int userid);
	
	@Select(UserSQL.R_USER_BY_USER_HASH)
	@Results(value={
			@Result(property="userId" , column="userid"),
			@Result(property="email" , column="email"),
			@Result(property="password" , column="password"),
			@Result(property="username" , column="username"),
			@Result(property="gender" , column="gender"),
			@Result(property="registeredDate" , column="registerdate"),
			@Result(property="userImageUrl" , column="userimageurl"),
			@Result(property="point" , column="point"),
			@Result(property="universityId" , column="universityid"),
			@Result(property="departmentId" , column="departmentid"),
			@Result(property="userStatus" , column="userstatus"),
			@Result(property="socialId" , column="sc_fb_id"),
			@Result(property="socialType" , column="sc_type"),
			@Result(property="isConfirmed" , column="isconfirmed"),
			@Result(property="signUpWith" , column="signup_with"),
			@Result(property="encUserId" , column="userid"),
			@Result(property="userHash" , column="user_hash"),
			@Result(property="status" , column="status"),
			@Result(property="roles" , column="userid" ,
					many = @Many(select = "findRolesByUserId")
			)
	})
	User findUserByUserHash(String userHash);
	
	
	
	// Update User status when verifying email -- Writer: Ean Sokchomrern, Date: 15/09/2016
	@Update(UserSQL.U_USER_VERIFY_EMAIL)
	public boolean updateUserVerifyEmail(String verification_code);
	
	// Register new user - Ean Sokchomrern, 16/09/2016
	@Insert(UserSQL.C_USER_REGISTER)
	public boolean insertUserRegister(UserRegister user);
	
	// Check user exists or not. If so, return verification_code to reset password. Ean Sokchomrern (19/09/2016)
	@Select(UserSQL.R_USER_EMAIL)
	public String getVerificationCodeByEmail(String email);
	
	// Update password by verification_code. Ean Sokchomrern (20/09/2016)
	@Update(UserSQL.U_USER_PASSWORD)
	public boolean updateUserPassword(UserRegister user);
	
	// Check email exists or not. If so, return 1 else return 0. Ean Sokchomrern (05/10/2016)
	@Select(UserSQL.R_USER_EMAIL_EXISTS)
	public int isIntEmailExists(String email);
	
	@Insert(UserSQL.C_USER_REGISTER_WITH_SCOIAL)
	public boolean insertUserWithScoial(FrmSocailUser user);
	
	
	
	/* Tola - For Mobile Application  */
	
	@Select(UserSQL.R_USER_BY_EMAIL_AND_PASSWORD)
	@Results(value={
			@Result(property="userId" , column="userid"),
			@Result(property="email" , column="email"),
			@Result(property="username" , column="username"),
			@Result(property="gender" , column="gender"),
			@Result(property="phonenumber" , column="phonenumber"),
			@Result(property="registeredDate" , column="registerdate"),
			@Result(property="userImageUrl" , column="userimageurl"),
			@Result(property="point" , column="point"),
			@Result(property="userHash" , column="user_hash"),
			@Result(property="roles" , column="userid" ,
					many = @Many(select = "findRolesByUserId")
			)
	})
	public UserInfo findUserByUserEmailAndPassword(UserMobileLogin userLogin);
	
	@Insert(UserSQL.C_USER_REGISTER_MOBILE)
	@SelectKey(statement="SELECT last_value FROM seq_user", keyProperty="userId", keyColumn="last_value", before=false, resultType=int.class)
	public boolean insertUserMobile(FrmUserRegister user);
	
	@Select(UserSQL.R_USER_BY_USER_ID)
	@Results(value={
			@Result(property="userId" , column="userid"),
			@Result(property="email" , column="email"),
			@Result(property="username" , column="username"),
			@Result(property="gender" , column="gender"),
			@Result(property="phonenumber" , column="phonenumber"),
			@Result(property="registeredDate" , column="registerdate"),
			@Result(property="userImageUrl" , column="userimageurl"),
			@Result(property="point" , column="point"),
			@Result(property="userHash" , column="user_hash"),
			@Result(property="roles" , column="userid" ,
					many = @Many(select = "findRolesByUserId")
			)
	})
	public UserInfo findUserByUserIdMobile(int userid);
	
	@Select(UserSQL.R_USER_BY_EMAIL)
	@Results(value={
			@Result(property="userId" , column="userid"),
			@Result(property="email" , column="email"),
			@Result(property="username" , column="username"),
			@Result(property="gender" , column="gender"),
			@Result(property="phonenumber" , column="phonenumber"),
			@Result(property="registeredDate" , column="registerdate"),
			@Result(property="userImageUrl" , column="userimageurl"),
			@Result(property="point" , column="point"),
			@Result(property="userHash" , column="user_hash"),
			@Result(property="roles" , column="userid" ,
					many = @Many(select = "findRolesByUserId")
			)
	})
	public UserInfo findUserByUserEmailMobile(String email);
	
	@Update(UserSQL.U_UPDATE_USER)
	public boolean updateUser(FrmUpdateUser user);
}
