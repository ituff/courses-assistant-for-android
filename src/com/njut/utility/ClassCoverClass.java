package com.njut.utility;

import java.util.ArrayList;
import java.util.List;

import org.nutlab.kczl.kczlApplication;

import com.njut.data.CourseElement;
import com.njut.data.Curriculum;

public class ClassCoverClass {

	public CourseElement curriculumToCourseElement(Curriculum curriculum) {
		CourseElement courseElement = new CourseElement(
				curriculum.getCourseName(), 0, curriculum.getTeacher(),
				curriculum.getPlace(), curriculum.getStartTime() + "-"
						+ curriculum.getEndTime(), curriculum.getCtid(),
				curriculum.getCourseNature(), curriculum.getCredit(),
				curriculum.getEndTime());
		return courseElement;
	}

	public List<CourseElement> curriculumsToCourseElements(
			List<Curriculum> curriculums) {
		List<CourseElement> courseElements = new ArrayList<CourseElement>();
		for (int i = 0; i < curriculums.size(); i++) {
			courseElements.add(curriculumToCourseElement(curriculums.get(i)));
		}
		return courseElements;
	}

	public List<CourseElement> curriculumsToCourseElements(
			List<Curriculum> curriculums, int day, int week) {
		List<CourseElement> courseElements = new ArrayList<CourseElement>();
		for (int i = 0; i < curriculums.size(); i++) {
			Curriculum curriculum = curriculums.get(i);
			if (day != curriculum.getDay())
				continue;
			if (week < curriculum.getBeginWeek()
					|| week > curriculum.getEndWeek())
				continue;
			if (week % 2 == 0 && curriculum.getOddorReven().equals("Ë«"))
				courseElements.add(curriculumToCourseElement(curriculum));
			if (week % 2 == 1 && curriculum.getOddorReven().equals("µ¥"))
				courseElements.add(curriculumToCourseElement(curriculum));
			if(curriculum.getOddorReven().trim().length()<1)
				courseElements.add(curriculumToCourseElement(curriculum));
		}
		sortHelper sHelper=new sortHelper();
		return sHelper.CourseElementDESC(courseElements);
	}
}
