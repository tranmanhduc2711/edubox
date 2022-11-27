package com.example.edubox.repository;

import com.example.edubox.constant.ECommonStatus;
import com.example.edubox.constant.ERoleType;
import com.example.edubox.entity.Group;
import com.example.edubox.entity.GroupMember;
import com.example.edubox.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Integer> {
    @Query("SELECT g.user FROM GroupMember g WHERE g.roleType = :type AND g.group.groupCode = :groupCode AND g.status= 'A' ")
    List<User> getGroupMembersByCodeAndMemberType(String groupCode, ERoleType type);

    @Query("SELECT g.user FROM GroupMember g WHERE g.group.groupCode =:code AND g.roleType = 'OWNER' AND g.status= 'A' ")
    User getGroupOwner(String code);

    @Query("SELECT g.user FROM GroupMember g WHERE  g.group.groupCode = :groupCode AND g.status = 'A' ")
    List<User> getGroupMembersByCode(String groupCode);

    @Query("SELECT g.user FROM GroupMember g WHERE g.group.groupCode = :groupCode AND g.user.code = :memberCode AND g.status='A' ")
    Optional<User> findMember(String groupCode,String memberCode);

    @Query("SELECT g.group FROM GroupMember g WHERE  g.user.code = :code AND g.status='A' ")
    List<Group> getGroupsByUserCode(String code);

    List<GroupMember> findAllByUserAndStatus(User user,ECommonStatus status);

    @Query("SELECT g.group FROM GroupMember g WHERE g.roleType = 'OWNER' AND g.user.code = :userCode AND g.status= 'A' ")
    List<Group> getGroupsCreatedByUser(String userCode);
}
