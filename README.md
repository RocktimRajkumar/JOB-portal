

# JOB-portal
**IP address of the API's has been changed pls follow the readme till end. Forked it and change it respectively.**
## Job Vibe 
 ![Sample Portfolio](https://github.com/Rocktim53/JOB-portal/blob/master/jvtie.png)

 # PROJECT DESCRIPTION
 # ===================
 Our project involved us to get familiar with the Android Development environment and thus develop an application 
 to serve as a personal companion to one. 
 So what our app does is fairly simple. It takes candidate details and find a Job that is suitable according to 
 his/her qualifications.

 # Features
 # ========
 We focussed our efforts to build an intuitive ,clean and simple UI. For the same, we encompassed card UI in our app. 
 We also implemented swipeable tabs so as to provide ease in navigating between different fragments.

# ScreenShot

## Splash Screen
 ![Sample Portfolio](https://github.com/Rocktim53/JOB-portal/blob/master/screenshots/screenshot/Splash.png)

## LogIn
The 'Login' activity is added in order to verify the credentials of an authorized user. The user needs to enter proper
credentials to access the rest of the functionalities.

 ![Sample Portfolio](https://github.com/Rocktim53/JOB-portal/blob/master/screenshots/screenshot/Login.png)

## SignUp
The 'Sign up' activity allows the user to register himself in Job Vibe. This will allow the user to search and apply for Jobs. 
During the sign up process, the user must first enter a valid email address followed by a password of his/her choice(minimum 8 charaters needed).
After this process is completed, the user will be asked to fill some more blanks regarding his/her personal and education information.

 ![Sample Portfolio](https://github.com/Rocktim53/JOB-portal/blob/master/screenshots/screenshot/Signup.png)

 ## Addition Information
 ![Sample Portfolio](https://github.com/Rocktim53/JOB-portal/blob/master/screenshots/screenshot/Addition_Information.png)

## Home
 This is first layout the user interacts with after his/her registration is completed. Here he/she will find two tabs headed as 'Matched' and 'Recommended'.
 While in 'Matched' the user will see jobs according to his qualification whereas in 'Recommended' he/she will see all the available jobs. The user can also
 filter the jobs according to his/her Skill, Location and preferred Company. He/she can also search job according to desired field of work,say for example
 Mobile App Development.

 ![Sample Portfolio](https://github.com/Rocktim53/JOB-portal/blob/master/screenshots/screenshot/MatchedRecommended.png)


## Activity
 It keeps track of user's activity within the app which includes the job he/she viewed, saved or applied.

 ![Sample Portfolio](https://github.com/Rocktim53/JOB-portal/blob/master/screenshots/screenshot/Activity.png)


## API
**Job Seeker  API**
| Path  | Description  |
|--|--|
|[http://103.230.103.93/jobportalapp/job.asmx?op=ApplyJob](http://103.230.103.93/jobportalapp/job.asmx?op=ApplyJob)  | Apply Job |
|[http://103.230.103.93/jobportalapp/job.asmx?op=CandidateLogin](http://103.230.103.93/jobportalapp/job.asmx?op=CandidateLogin)|Candidate Login|
|[http://103.230.103.93/jobportalapp/job.asmx?op=EditCandidateEducationalDetails](http://103.230.103.93/jobportalapp/job.asmx?op=EditCandidateEducationalDetails)|Edit Candidate Education Details|
|[http://103.230.103.93/jobportalapp/job.asmx?op=EditCandidatePersonalDetails](http://103.230.103.93/jobportalapp/job.asmx?op=EditCandidatePersonalDetails)|Edit Candidate Personal Details|
|[http://103.230.103.93/jobportalapp/job.asmx?op=GetBranch](http://103.230.103.93/jobportalapp/job.asmx?op=GetBranch)|Get Branch|
|[http://103.230.103.93/jobportalapp/job.asmx?op=GetCandidateDetails](http://103.230.103.93/jobportalapp/job.asmx?op=GetCandidateDetails)|Get Candidate Details|
|[http://103.230.103.93/jobportalapp/job.asmx?op=GetCourse](http://103.230.103.93/jobportalapp/job.asmx?op=GetCourse)|Get Course|
|[http://103.230.103.93/jobportalapp/job.asmx?op=GetJobDetails](http://103.230.103.93/jobportalapp/job.asmx?op=GetJobDetails)|Get Job Details|
|[http://103.230.103.93/jobportalapp/job.asmx?op=GetLocation](http://103.230.103.93/jobportalapp/job.asmx?op=GetLocation)|Get Location|
|[http://103.230.103.93/jobportalapp/job.asmx?op=GetResponse](http://103.230.103.93/jobportalapp/job.asmx?op=GetResponse)|Get Response|
|[http://103.230.103.93/jobportalapp/job.asmx?op=GetSkill](http://103.230.103.93/jobportalapp/job.asmx?op=GetSkill)|Get Skill|
|[http://103.230.103.93/jobportalapp/job.asmx?op=JobSearch](http://103.230.103.93/jobportalapp/job.asmx?op=JobSearch)|Search Job|
|[http://103.230.103.93/jobportalapp/job.asmx?op=SaveCandidate](http://103.230.103.93/jobportalapp/job.asmx?op=SaveCandidate)|Save Candidate|
|||

**Recruiter API**
| Path | Description |
|--|--|
| [http://103.230.103.93/jobportalapp/job.asmx?op=CompanyLogin](http://103.230.103.93/jobportalapp/job.asmx?op=CompanyLogin) | Company Login |
|[http://103.230.103.93/jobportalapp/job.asmx?op=EditCompanyProfile](http://103.230.103.93/jobportalapp/job.asmx?op=EditCompanyProfile)|Edit Company Profile|
|[http://103.230.103.93/jobportalapp/job.asmx?op=GetInterviewSchduleDate](http://103.230.103.93/jobportalapp/job.asmx?op=GetInterviewSchduleDate)|Get Interview Schedule Date|
|[http://103.230.103.93/jobportalapp/job.asmx?op=InterviewDateSchdule](http://103.230.103.93/jobportalapp/job.asmx?op=InterviewDateSchdule)| Interview Date Schedule|
|[http://103.230.103.93/jobportalapp/job.asmx?op=SaveCompany](http://103.230.103.93/jobportalapp/job.asmx?op=SaveCompany)|Save Company|
| | |

