package com.LakeSide.LakeSide.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LakeSide.LakeSide.model.Room;
import com.LakeSide.LakeSide.model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{

	@Override
	public <S extends UserAccount> S save(S entity);
}
