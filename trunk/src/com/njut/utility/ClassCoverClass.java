package com.njut.utility;

import java.util.ArrayList;
import java.util.List;

import com.njut.data.CourseElement;
import com.njut.data.Curriculum;

public class ClassCoverClass {
	
	public CourseElement curriculumToCourseElement(Curriculum curriculum) {
		CourseElement courseElement=new CourseElement(curriculum.getCourseName(), 0, curriculum.getTeacher(), curriculum.getPlace(), curriculum.getStartTime()+"-"+curriculum.getEndTime());
		return courseElement;
	}

	public List<CourseElement> curriculumsToCourseElements(List<Curriculum> curriculums) {
		List<CourseElement> courseElements=new ArrayList<CourseElement>();
		for(int i=0;i<curriculums.size();i++){
			courseElements.add(curriculumToCourseElement(curriculums.get(i)));			
		}
		return courseElements;
	}
}
