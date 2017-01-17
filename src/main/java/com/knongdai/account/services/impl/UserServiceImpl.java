package com.knongdai.account.services.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.knongdai.account.entities.User;
import com.knongdai.account.entities.UserRegister;
import com.knongdai.account.entities.forms.FrmSocailUser;
import com.knongdai.account.entities.forms.FrmUpdateUser;
import com.knongdai.account.entities.forms.FrmUserRegister;
import com.knongdai.account.entities.forms.UserInfo;
import com.knongdai.account.entities.forms.UserLogin;
import com.knongdai.account.entities.forms.UserMobileLogin;
import com.knongdai.account.repositories.UserRepository;
import com.knongdai.account.services.UserService;
import com.knongdai.account.utilities.Encryption;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User findUserByEmail(UserLogin userLogin) {		
		return userRepository.findUserByEmail(userLogin);
	}

	@Override
	public User findUserByUserId(String userid) {
		// TODO Auto-generated method stub
		return userRepository.findUserByUserId(Integer.parseInt(Encryption.decode(userid)));
	}

	// Update User status when verifying email -- Writer: Ean Sokchomrern, Date: 15/09/2016
	@Override
	public boolean updateUserVerifyEmail(String verification_code) {
		// TODO Auto-generated method stub
		return userRepository.updateUserVerifyEmail(verification_code);
	}

	// Register new user - Ean Sokchomrern, 16/09/2016
	@Override
	public boolean insertUserRegister(UserRegister user) {
		// TODO Auto-generated method stub
		return userRepository.insertUserRegister(user);
	}

	// Check user exists or not. If so, return verification_code to reset password. Ean Sokchomrern (19/09/2016)
	@Override
	public String getVerificationCodeByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.getVerificationCodeByEmail(email);
	}

	// Update password by verification_code. Ean Sokchomrern (20/09/2016)
	@Override
	public boolean updateUserPassword(UserRegister user) {
		// TODO Auto-generated method stub
		return userRepository.updateUserPassword(user);
	}

	//TODO : Find User by user hash 
	@Override
	public User findUserByUserHash(String userHash) {
		return userRepository.findUserByUserHash(userHash);
	}

	@Override
	public int isIntEmailExists(String email) {
		// TODO Auto-generated method stub
		return userRepository.isIntEmailExists(email);
	}

	@Override
	public boolean insertUserWithScoial(FrmSocailUser user) {
		// TODO Auto-generated method stub
		return userRepository.insertUserWithScoial(user);
	}

	@Override
	public UserInfo findUserByUserEmailAndPassword(UserMobileLogin userLogin) {
		// TODO Auto-generated method stub
		return userRepository.findUserByUserEmailAndPassword(userLogin);
	}

	@Override
	public boolean insertUserMobile(FrmUserRegister user) {
		// TODO Auto-generated method stub
		return userRepository.insertUserMobile(user);
	}

	@Override
	public UserInfo findUserByUserIdMobile(int userId) {
		// TODO Auto-generated method stub
		return userRepository.findUserByUserIdMobile(userId);
	}

	@Override
	public boolean updateUser(FrmUpdateUser user) {
		// TODO Auto-generated method stub
		return userRepository.updateUser(user);
	}

	@Override
	public UserInfo findUserByUserEmailMobile(UserLogin userLogin) {
		// TODO Auto-generated method stub
		return userRepository.findUserByUserEmailMobile(userLogin.getEmail());
	}


	
	
		

}
