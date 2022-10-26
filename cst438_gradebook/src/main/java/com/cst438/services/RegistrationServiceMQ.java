package com.cst438.services;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class RegistrationServiceMQ extends RegistrationService {

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public RegistrationServiceMQ() {
		System.out.println("MQ registration service ");
	}

	// ----- configuration of message queues
	@Autowired
	Queue registrationQueue;
	// ----- end of configuration of message queue

	
	// receiver of messages from Registration service
	@RabbitListener(queues = "gradebook-queue")
	@Transactional
	public void receive(EnrollmentDTO enrollmentDTO) {
		
		//TODO  complete this method in homework 4
		System.out.println("Receive course :" + enrollmentDTO);

		// process the list of student grades
		for (CourseDTOG.GradeDTO g : enrollmentDTO.grades) {
			Enrollment e = enrollmentRepository.findByEmailAndCourseId(g.student_email, enrollmentDTO.course_id);
			e.setCourseGrade(g.grade);
			enrollmentRepository.save(e);
			System.out.println("final grade update " + g.student_email + " " + enrollmentDTO.course_id + " " + g.grade);
		}
		
	}

	// sender of messages to Registration Service
	@Override
	public void sendFinalGrades(int course_id, CourseDTOG courseDTO) {
		 
		//TODO  complete this method in homework 4
		
		rabbitTemplate.convertAndSend(registrationQueue.getName(), courseDTO);
		
		System.out.println("Message send to registration service for course " + course_id);

	}

}
