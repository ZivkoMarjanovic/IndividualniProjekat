package com.tus.individual.project.repository;

import com.tus.individual.project.model.Order;

import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Query ("SELECT o FROM Order o "
			+ "WHERE o.status = com.tus.individual.project.model.StatusEnum.PAID "
			+ "OR o.status = com.tus.individual.project.model.StatusEnum.SERVED "
			+ "ORDER BY o.created DESC")
	List<Order> findAllInStatusPaidAndServed();

	@Query ("SELECT SUM(o.order_total) AS total,  COUNT(*) AS number, DATE(o.created) AS day "
			+ "FROM Order AS o "
			+ "WHERE o.created > :dateFrom "
			+ "AND o.status in (com.tus.individual.project.model.StatusEnum.PAID,  com.tus.individual.project.model.StatusEnum.SERVED, com.tus.individual.project.model.StatusEnum.COLLECTED) "
			+ "GROUP BY DAY(o.created) ")
	List<Tuple> getWeeklyReport (@Param("dateFrom") Date dateFrom);

}
