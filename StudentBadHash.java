package assign09;

import java.text.DecimalFormat;

/**
 * Represents a University of Utah student with a weak hash function.
 * Students with first names of the same length receive the same hash code.
 *
 * @author CS 2420 course staff and Yujia Zhao and Ava Murphy
 * @version November 11, 2026
 */
public class StudentBadHash {

	private int uid;
	private String firstName;
	private String lastName;

	/**
	 * Constructs a student with the specified UID and names.
	 *
	 * @param uid the student's university ID
	 * @param firstName the student's first name
	 * @param lastName the student's last name
	 */
	public StudentBadHash(int uid, String firstName, String lastName) {
		this.uid = uid;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * Returns this student's university ID.
	 *
	 * @return the student's UID
	 */
	public int getUid() {
		return this.uid;
	}

	/**
	 * Returns this student's first name.
	 *
	 * @return the student's first name
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Returns this student's last name.
	 *
	 * @return the student's last name
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Determines whether this student and another object have the same
	 * UID, first name, and last name.
	 *
	 * @param other the object to compare with this student
	 * @return true if both students have the same UID and names, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof StudentBadHash))
			return false;

		StudentBadHash rhs = (StudentBadHash) other;

		return this.uid == rhs.uid && this.firstName.equals(rhs.firstName) && this.lastName.equals(rhs.lastName);
	}

	/**
	 * Returns a textual representation of this student.
	 *
	 * @return the student's full name and formatted UID
	 */
	@Override
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("0000000");
		return firstName + " " + lastName + " (u" + formatter.format(uid) + ")";
	}

	/**
	 * Computes a hash code for this student.
	 * Uses only the first name length, which causes many collisions.
	 * Equal students always produce equal hash codes.
	 *
	 * @return the student's hash code
	 */
	@Override
	public int hashCode() {
		return firstName.length();
	}
}
