package com.example.DAL;

public class GetSetters {
	
	  private  long id;
	  private String firstname;
	  private String lastname;
	  private String phonenumber;

	public  long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getFirstname() {
	    return firstname;
	  }

	  public void setFirstname(String firstname) {
	    this.firstname = firstname;
	  }
	  
	  
	  public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
		
		public String getPhonenumber() {
			return phonenumber;
		}

		public void setPhonenumber(String phonenumber) {
			this.phonenumber = phonenumber;
		}

		
		
	  @Override
	  public String toString() {
		  
		  return ""+this.firstname + "   " + this.lastname + "    " + this.phonenumber + "";
	  }
}
