# Placement-Portal
> A Mobile Application which in coordination with Placement Cell can help students and companies to reach each other.

![Opening Page](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/start_screen.jpg)

## Main Features
- [Login Page](#login-page)
- [Sign-Up Student](#student-sign-up)
- [Sign-Up Company](#company-sign-up)
- [Profile Page](#profile-page)
- [Profile Updation & Changing Password](#profile-updation--changing-password)
- [Internships/Jobs Page](#internshipsjobs-page)
- [Intern/Job Description Page](#internjob-description-page)
- [Applications Page](#applications-page)
- [Add Intern/Job Page](#add-internjob-page)
- [Applicants Page](#applicants-page)

### Login Page
![Login](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/login_student.jpg)
- User Login by providing suitable details, which are verified by Google Firebase Database.
- Each user has a unique user-id, which is used as key in storing the user in Database.
- When successfully loggged in, user is directed to suitable Landing Page.
- If provided details are incorrect, an error message is displayed.
- If user is registered but account is'nt verified, suitable prompt is indicated.
- User has an option to directly goto Signup page if required.

### Student Sign-Up 
![Student Sign-Up](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/signup_student.jpg)
- Student register his/her account by providing the details which get saved in the Database.
- Student can select his/her branch by choosing from the list when select branch option is clicked.
- If App is permitted to access storage then profile pic is selected else suitable prompt asking for permission is displayed.
- Student's profile pic is saved in Firebase Storage while the url of image gets saved in database.
- After successfully registering, gets directed to login page, with a prompt to verify account using the verification link sent to registered email address.

### Company Sign-Up
![Company Sign-Up](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/signup_company.jpg)
- Company register its account by providing the details which get saved in the Database.
- Comapany's profile pic is saved in Firebase Storage while the url of image gets saved in database.
- After successfully registering, gets directed to login page, with a prompt to verify account using the verification link sent to registered email address.

### Profile Page
![Profile Page](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/profile_student.jpg)
- Profile Page displays the details of the student/company by extraction correct dataSnapshot of the Database.
- Profile pic is loaded by Picasso using the url provided through dataSnapshot.
- User has the option to change the profile pic by first choosing the pic from the device storage and then by clicking update. As the image is stored at address defined by user's id; so previous image is replaced by new and it's url is updated in database.
- User can also choose to update the details by going to update details page.
- password can be changed by clicking on "Change Password" button.

### Profile Updation & Changing Password
![Profile Updation & Changing Password](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/profile_update.jpg)
![Profile Updation & Changing Password](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/change_password.jpg)
- User is able to update the details on the database, for students option for branch change is also provided.
- For changing password, user has to provide current password also for verification purposes.
- on successfully completion of task, user is duly prompted.

### Internships/Jobs Page
![Internships/Jobs Page](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/internships.jpg)
![Internships/Jobs Page](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/jobs.jpg)
- These pages list all the internships/job offers available for his/her branch in an organised manner.
- Each offer lists the company name and position along with company logo; user can also check an offer's details.
- All the values are presented by traversing though the database and providing snapshots at each instance.

### Intern/Job Description Page
![Intern/Job Description Page](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/intern_description.jpg)
- Description page provide all the details about the offer; namely: company name, position, job description, branches allowed, cut-off cpi, date of commencement.
- Values are presented by accessing intern-id / job-id(provided by earlier page) on Database.
- Page provides the option to download the brochure( only when App has permissions to change internal storage, else prompted for permissions).
- Brochure is downloaded by the Download Manager which is provided with the brochure url; application must be connnected to internet all times.
- Student can also apply for the offer( if interested ); when applying suitable checks are done like cutoff cpi, branch, date of application & commencement.
- When successfully applied, user is prompted; he/she can only apply once for an offer.

### Applications Page
![Applications Page](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/applications.jpg)
- This page displays all the intern/job offers he/she has applied to in an orderly fashion.
- Each item include offer type, position, company's name and company's email for communication purposes.

### Add Intern/Job Page
![Add Intern/Job Page](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/applicants.jpg)
- On the add offer page provided to company, the company can add intern/job oppurtunities by providing the requirements for each.
- Details like Position offered, job description, cutoff CPI are provided as an editText value
- For commencement date a DatePickerDialog box opens up; later the date is formatted to desired format.
- Brochure is selected from the phone's internal storage and saved in the Firebase Storage and url is stored in database.
- Each offer is provided with a unique id and is stored/accessed in the database accordingly.

### Applicants Page
![Applicants Page](https://github.com/Harshal-13/Placement-Portal/blob/master/Placement/app/appImages/applicants.jpg)
- The Company can view all the applicants who have applied to the offers provided by that company in an orderly manner.
- Each item include offer type, position, student's name and student's email for communication purposes.
