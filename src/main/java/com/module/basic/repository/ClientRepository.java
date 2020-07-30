package com.module.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.module.basic.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
	public Client findByEmailAndPwd(String email, String pwd);
}
