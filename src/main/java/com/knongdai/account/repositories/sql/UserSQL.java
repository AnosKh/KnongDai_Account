package com.knongdai.account.repositories.sql;

public interface UserSQL {
	
	String R_USER_BY_EMAIL="SELECT"
			+ "		U.userid,"
			+ " 	U.email,"
			+ " 	U.password,"
			+ " 	U.gender,"
			+ " 	U.dateofbirth,"
			+ " 	U.phonenumber,"
			+ " 	U.registerdate,"
			+ " 	U.userimageurl,"
			+ " 	U.point,"
			+ "		U.universityid,"
			+ " 	U.departmentid,"
			+ " 	U.userstatus,"
			+ " 	U.sc_fb_id,"
			+ " 	U.sc_type,"
			+ " 	U.isconfirmed,"
			+ " 	U.signup_with,"
			+ "     U.username,"
			+ "     U.user_hash,"
			+ "     U.status,"            // new field store status such as '0': Inactive, '1': Active, '2': Deleted, '3': Locked (Ean Sokchomrern, 15/09/2016)
			+ "     U.verification_code"  // new field store verification_code ((Ean Sokchomrern, 15/09/2016)
			+ " FROM"
			+ " 	tbluser U"
			+ " WHERE"
			+ " 	U.email = #{email} ";
	
	String R_ROLES_BY_USER_ID = "SELECT"
			+ "		UR.role_id , UR.user_id , R.role_name"
			+ " FROM"
			+ " 	tbluser_role UR"
			+ " INNER JOIN tblrole R ON UR.role_id = R.role_id"
			+ " WHERE"
			+ " 	UR.user_id = #{userId}";
	
	 String R_USER_BY_USER_ID="SELECT"
				+ "		U.userid,"
				+ " 	U.email,"
				+ " 	U.password,"
				+ " 	U.gender,"
				+ " 	U.dateofbirth,"
				+ " 	U.phonenumber,"
				+ " 	U.registerdate,"
				+ " 	U.userimageurl,"
				+ " 	U.point,"
				+ "		U.universityid,"
				+ " 	U.departmentid,"
				+ " 	U.userstatus,"
				+ " 	U.sc_fb_id,"
				+ " 	U.sc_type,"
				+ " 	U.isconfirmed,"
				+ " 	U.signup_with,"
				+ "     U.user_hash,"
				+ "     U.status,"            // new field store status such as '0': Inactive, '1': Active, '2': Deleted, '3': Locked (Ean Sokchomrern, 15/09/2016)
				+ "     U.username"
				+ " FROM"
				+ " 	tbluser U"
				+ " WHERE"
				+ " 	U.userid = #{userid} ";
	 
	 String R_USER_BY_USER_HASH="SELECT"
				+ "		U.userid,"
				+ " 	U.email,"
				+ " 	U.password,"
				+ " 	U.gender,"
				+ " 	U.dateofbirth,"
				+ " 	U.phonenumber,"
				+ " 	U.registerdate,"
				+ " 	U.userimageurl,"
				+ " 	U.point,"
				+ "		U.universityid,"
				+ " 	U.departmentid,"
				+ " 	U.userstatus,"
				+ " 	U.sc_fb_id,"
				+ " 	U.sc_type,"
				+ " 	U.isconfirmed,"
				+ " 	U.signup_with,"
				+ "     U.user_hash,"
				+ "     U.status,"            // new field store status such as '0': Inactive, '1': Active, '2': Deleted, '3': Locked (Ean Sokchomrern, 15/09/2016)
				+ "     U.username"
				+ " FROM"
				+ " 	tbluser U"
				+ " WHERE"
				+ " 	U.user_hash = #{userHash} ";
	 
	 String C_USER="INSERT INTO tbluser ( email , username , password , gender , dateofbirth , phonenumber , registerdate , userimageurl , point , universityid,  departmentid, userstatus , sc_fb_id , sc_type  , isconfirmed , signup_with  ) VALUES ()";
	 
	
	 // Update User status when verifying email -- Writer: Ean Sokchomrern, Date: 15/09/2016
	String U_USER_VERIFY_EMAIL = "UPDATE tbluser SET status = '1' WHERE verification_code = #{verification_code}";
	
	
	
	// Register new user - Ean Sokchomrern, 16/09/2016
//	String C_USER_REGISTER = "INSERT INTO tbluser (userid, email, username, password, gender, registerdate, verification_code) "+ 
//							 " VALUES(nextval('seq_user'), #email, #username, #password, #gender, now(), #verification_code) ";
//	

	String C_USER_REGISTER = " INSERT INTO tbluser (userid, email, username, password, gender, registerdate, verification_code) "+ 
			" VALUES(nextval('seq_user'), #{email}, #{username}, #{password}, #{gender}, now(),#{verification_code}) "; 
	
	// Check user exists or not. Ean Sokchomrern (19/09/2016)
	String R_USER_EMAIL = "SELECT verification_code FROM tbluser WHERE email=#{email}";
	
	// Update password by verification_code. Ean Sokchomrern (20/09/2016)
	String U_USER_PASSWORD = "UPDATE tbluser SET password = #{password} WHERE verification_code = #{verification_code} ";

	// Check email exists or not. Ean Sokchomrern (05/10/2016)
	String R_USER_EMAIL_EXISTS = "SELECT COUNT(email) FROM tbluser WHERE email=#{email}";
	
	String C_USER_REGISTER_WITH_SCOIAL = " INSERT INTO tbluser (userid, email, username, password, gender, registerdate, status , userimageurl, sc_type , sc_fb_id , signup_with) "+ 
			" VALUES(nextval('seq_user'), #{email}, #{username}, #{password}, #{gender}, now(),'1' , #{userImageUrl} , #{socialType} , #{socialId} , #{signUpWith} ) "; 
	
	
	String R_USER_BY_EMAIL_AND_PASSWORD="SELECT"
			+ "		U.userid,"
			+ " 	U.email,"
			+ " 	U.username,"
			+ " 	U.gender,"
			+ " 	U.dateofbirth,"
			+ " 	U.phonenumber,"
			+ " 	U.registerdate,"
			+ " 	U.userimageurl,"
			+ " 	U.point,"
			+ "		U.universityid,"
			+ " 	U.departmentid,"
			+ "     U.user_hash,"
			+ "     U.status "
			+ " FROM"
			+ " 	tbluser U"
			+ " WHERE"
			+ " 	U.email = #{email} AND U.password = #{password}";
	
	String C_USER_REGISTER_MOBILE = " INSERT INTO tbluser (userid, email, username, password, gender, registerdate, status , userimageurl,  signup_with , verification_code) "+ 
			" VALUES(nextval('seq_user'), #{email}, #{username}, #{password}, #{gender}, now(),'0' , #{userImageUrl} ,  #{signUpWith} , #{verification_code} ) "; 
	
	String U_UPDATE_USER = "UPDATE tbluser SET username = #{username} , gender = #{gender}  , dateofbirth  = #{dateOfBirth} , phonenumber  = #{phonenumber}  , userimageurl  = #{userImageUrl} WHERE userid = #{userId} ";

}
