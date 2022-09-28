package com.cst438.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentListDTO;
import com.cst438.domain.AssignmentListDTO.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.GradebookDTO;
import com.cst438.services.RegistrationService;

@RestController
public class AssignmentController {
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	AssignmentGradeRepository assignmentGradeRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	RegistrationService registrationService;
	
//	As an instructor for a course , I can add a new 
//	assignment for my course.  The assignment has a name and a due date.
	@PostMapping("/gradebook")
	public AssignmentListDTO addAssignment (@RequestBody AssignmentListDTO a) {
		
		Assignment newAssignment = new Assignment();
		newAssignment.setAssignmentName(a.assignmentName);
		newAssignment.setDueDate(a.dueDate);
		newAssignment = courseRepository.save(newAssignment);
		a.assignmentId = newAssignment.getAssignmentId();
		return a;
	}

//	As an instructor, I can change the name of the assignment for my course.

//	As an instructor, I can delete an assignment  for my course (only if 
//	there are no grades for the assignment).
	
}
