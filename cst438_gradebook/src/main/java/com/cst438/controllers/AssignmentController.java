package com.cst438.controllers;

//import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentListDTO;
import com.cst438.domain.AssignmentListDTO.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
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
	@GetMapping("/getAssignment")
	@Transactional
	  public AssignmentListDTO.AssignmentDTO getExample(){
	    Assignment a = assignmentRepository.findById(1).orElse(null);

	    return new AssignmentListDTO.AssignmentDTO(a.getId(), a.getCourse().getCourse_id(), a.getName(), a.getDueDate().toString(), a.getCourse().getTitle());
	  }
	
	@PostMapping("/assignment")
	public Integer addAssignment (@RequestBody AssignmentListDTO.AssignmentDTO a) {
		
	System.out.println(""+a);
		
		int i = -1;
		
		Course assignmentCourse = courseRepository.findById(a.courseId).orElse(null);
			if (assignmentCourse == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "course not found");
			}
			else {
				Assignment newAssignment = new Assignment();
				newAssignment.setCourse(assignmentCourse);
				newAssignment.setName(a.assignmentName);
				newAssignment.setDueDate(java.sql.Date.valueOf(a.dueDate));
				newAssignment = assignmentRepository.save(newAssignment);
				
// parameter for assignmentdto(assignmentId, courseId, assignmentName, dueDate, courseTitle)

				for(Assignment iter : assignmentRepository.findAll()){
					if (iter.getCourse().equals(newAssignment.getCourse()) && iter.getDueDate().equals(newAssignment.getDueDate()) && iter.getName().equals(newAssignment.getName())) {
						i = iter.getId();
					}
				}
				//{Set the rest of the outAssignmentDTO variables}
			}

		return i; //return outAssignment
	}
	

//	As an instructor, I can change the name of the assignment for my course.
	//"/assignment/{assignmentId}"
	@PatchMapping("/assignment/{id}/{name}") //for the url in postman
	public void updateAssignment(@PathVariable("id") Integer id, @PathVariable("name") String newName) {
		Assignment temp = null;
		for(Assignment curAssign : assignmentRepository.findAll()) {
			if (curAssign.getId() == id) {
				temp = curAssign;
			}
		}
		if (temp == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id not found");
		}
		else {
			temp.setName(newName); //set new name
			assignmentRepository.save(temp); //save new name to the id in repo
		}
	}
	

//	As an instructor, I can delete an assignment  for my course (only if 
//	there are no grades for the assignment).
	@DeleteMapping("/assignment/{id}")
	public void deleteAssignment(@PathVariable("id") Integer id) {
		Assignment temp = null;
		for(Assignment curAssign : assignmentRepository.findAll()) {
			if (curAssign.getId() == id) {
				temp = curAssign;
			}
		}
		if (temp == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id not found");
		}
		else {
			assignmentRepository.delete(temp); //delete id in repo
		}
	}
}
