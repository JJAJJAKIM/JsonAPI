package com.app.mapper;

import com.app.dto.LoginDTO;
import com.app.dto.RoleDTO;
import com.app.dto.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface AuthMapper {

    @Select("select userNo, userNm from user " +
            "where userEnable = 1 " +
            "and userNm = #{userNm}  and userPwd = #{userPwd} ")
    public UserDTO login(LoginDTO loginDTO);

    @Select("select userNo, userNm from user " +
            "where userEnable = 1 " +
            "and userNm = #{userNm}  and userPwd = #{userPwd} ")
    public UserDTO login2(Map<String,String> loginDTO);

    @Select("select roleNm from user_role as ur " +
            "inner join role as r " +
            "on ur.roleNo = r.roleNo " +
            "and ur.userNo = #{userNo}")
    public List<RoleDTO> findByRoles(UserDTO userDTO);

    @Select("select userNo, userNm, userPwd, userEnable from user " +
            "where userNm = #{userNm}")
    public Optional<UserDTO> findbyUserNm(String userNm);

    @SelectKey(statementType = StatementType.PREPARED, statement = "select last_insert_id() as userNo", keyProperty = "userNo", before = false, resultType = int.class)
    @Insert("insert into user (userNm, userPwd) values ( #{userNm}, #{userPwd})")
    public int saveUser(UserDTO userDTO);

    @Insert("insert into user_role values (#{userNo}, 2 )")
    public int saveRole(UserDTO userDTO);
}
