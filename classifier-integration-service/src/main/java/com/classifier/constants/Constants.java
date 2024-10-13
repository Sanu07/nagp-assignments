package com.classifier.constants;

public interface Constants {

    String PROMPT_SANITIZE_TEXT = """
            # Consider yourself as a text sanitization model
                        \s
             ## Given a text, in the form of <speaker>: <conversation>, you need to sanitize the text following the below rules
            
             1. Find the interviewee and interviewer name from the complete context.
            
             2. Keep the conversation only from the interviewee and interviewer and remove the other conversations
            
             3. In every conversation of the interviewer, read the conversation and comprehend the conversation to the exact question asked by the interviewer
            
             4. Keep the answer of the interviewee as is. Do not make any change in that
            
             5. If there is any followup question, try to merge the conversations for the interviewer and also the conversations from the interviewee.
            
             6. Just list down the exact question asked and remove unnecessary conversation. Also remove the feedback from the interview like Okay, Thats's correct , in between asking question.Only the relevant question should be there.
            
             7. If there is a hint from the interviewer on the answers, do not remove that conversation and keep the exact question as it is to assess the candidate better.
            
                 **For Example**
                 user1: OK, we'll start the interview now, so What is Garbage Collection?
                 user2: Garbage collection is process of cleaning java memory.
                 user1: Correct. How many types are there?
                 user2: there are 4 types
                 user1: What are the different design patterns used in Java?
                 user2: I've worked on creational design patterns that are concerned with how to create objects. Not sure about the\s
                             other types of patterns.
                 user1: Anything on Structural and Behavioral design patterns?
                 user2:  yes, Structural design patterns focus on the composition of classes or objects to form larger, more complex\s
                              structures. And Behavioral design patterns deal with the communication and interaction between objects and\s
                              classes
                \s
                 **Output**
                 user1: What is Garbage Collection?how many types are there?
                 user2: Garbage collection is process of cleaning java memory. there are 4 types.
                 user1: What are the different design patterns used in Java?
                 user2:  I've worked on creational design patterns that are concerned with how to create objects. Not sure about the\s
                             other types of patterns.
                 user1: Anything on Structural and Behavioral design patterns?
                 user2:  yes, Structural design patterns focus on the composition of classes or objects to form larger, more complex\s
                              structures. And Behavioral design patterns deal with the communication and interaction between objects and\s
                              classes.
                \s
                 *Provide the final output in the below JSON format. It should provide the interviewer name and interviewee name along with list of questions and answers after sanitizing it based on the above rules.* **NO PREAMBLE**
                 {
                     "interviewer": "<name>",
                     "interviewee": "<name>",
                     "conversations": [
                         {
                             "question": "<question>"
                             "answer": "<answer>"
                         }
                     ]
                 }
            """;

    String PROMPT_INDIVIDUAL_REPORT_ANALYSIS = """
            # Consider yourself as a Java Microservice Interview Evaluation model
                        \s
             ## Given a list of questions and answers on the below *Interview Topics* and *Input Format*, you need to provide score and feedback following the *Set of Rules*
            
             ### Interview Topics
             1. Microservice
             2. Java
             3. DevOps
             4. MySQL Database
             5. Springboot
            
             ### Input Format
             {
                 "experience": "<years of experience of the candidate>"
                 "questionsAndAnswers": [
                     {
                         "question": "<question>",
                         "answer": "<answer>",
                         "technology": "<Interview Topic>",
                         "difficulty": "<difficulty level from 1 to 5, 1 being lowest>"
                     }
                 ]
             }
            
             ### Set of RULES
             1. **You should consider the experience of the candidate along with the question difficulty while evaluating**.
                 - *"experience": "<0-3>"* Check whether the answer contains basic understanding of the concept or not. If yes, provide full mark(5)
            
                 - *"experience": "<3.1-8>"* Check whether the answer covers major relevant areas.
            
                 - *"experience": "<8.1-20>"* Check whether the answer covers in-depth knowledge along with scope of practical implementations or any example.
            
             2. Provide a score for each question-answer pair from 1 to 5, where 1 is the lowest and     5 is the highest. The score should reflect how well the answer meets expectations       based on the candidate's experience level.
                 - Give full marks (5) if the major areas are covered appropriately for their          experience level.
            
             3. If the score is less than 4, provide polite and constructive feedback highlighting only the major missing or incorrect points. The feedback should be tailored to the experience level and politely encourage improvement.
            
             4. Provide the output in the below JSON *Output format* for every set of question and answer in the same order. Please follow below guidelines for the final output
            
             5. Avoid addressing the candidate directly. Instead, refer to the candidate in the third person to maintain a professional tone suited for panel review.
                        \s
                 a. *evaluation* contains marks and feedback for every set of question and answer in the same order
            
                 b. *analysis* contains a separate JSON object.
                 - "interviewComplexity" denotes the overall complexity of the interview process.
                 - "overall" is a key-value pair where each interview topic gets an overall score based on the questions asked and their complexity. If no questions were asked on a particular topic, leave it as empty.
                 - "positiveFeedback" indicates a list of major strengths shown by the candidate.
                 - "negativeFeedback" highlights the major areas for improvement.
                        \s
             **Output Format**
             {
                 "evaluation": [
                     {
                         "marksObtained": <mark between 1 to 5.s
                         **Consider the experience and question difficulty as mentioned in the first point in 'Set of RULES'**>,
                         "feedback": [
                             <list of feedback of the candidate>
                         ]
                     }
                 ],
                 "analysis": {
                     "interviewComplexity": "<rating between 1 to 5, 1 being lowest based on the complexity of the questions asked>",
                     "positiveFeedback": [
                         "should highlight list of positive areas"
                     ],
                     "areasOfImprovement": [
                         "should highlight the areas of improvements"
                     ],
                     "overall": {
                         "microservice": "<overall microservice knowledge with rating between 1 - 5, 1 being lowest>",
                         "devops": "<overall devops knowledge with rating between 1 - 5, 1 being lowest>",
                         "database": "<overall database knowledge with rating between 1 - 5, 1 being lowest>",
                         "java": "<overall java knowledge with rating between 1 - 5, 1 being lowest>",
                         "springboot": "<overall springboot knowledge with rating between 1 - 5, 1 being lowest>"
                     }
                 }
             }
            """;

    String PROMPT_COMPARE_AND_RANK_CANDIDATES = """
            # consider yourself as an interviewer analyst
              ## your job is to compare multiple JSON Input responses that contains the
              evaluated results on 5 different topics and rank the candidates based on their performance following the below
              *mentioned Guidelines* and the *job description provided*
              \s
              **Topics**
              1. microservice
              2. devops
              3. java
              4. springboot
              5. database
              \s
              ## Guidelines to rank the candidates
              1. consider the overall average score in each topic and also *consider interview   complexity* for evaluation.
              2. Read the complete Job description first and analyse the requirements of
              technologies   and experience. Based on your analysis assign weightage to the technologies and also to experience.
              3. Now based on   your assigned weightage to technologies and experience, evaluate and rank the candidates on their experience and  overall score obtained for individual technologies.
              4. Give an in-depth feedback in *LIST* format on why the particular candidate is chosen over other candidates. The feedback should be solely based on overall score obtained and experience
              5. Interview complexity 1 being the lowest and 5 being the highest, rank candidates accordingly.
              6. Give less preference to experience for rank calculation if a candidate with low experience is able to do well in higher complexity interview.
              7.Experience if a year less than the required experience, candidate can be considered for the position.
              8. Provide an output in given JSON
               \s
              ## Input JSON Format
              {
                "name": "<name of the candidate>",
                "interviewComplexity": "<number between 1 to 5, 1 as lowest>",
                "experience": "<in years>",
                "overall": {
                  "microservice": "<number between 1 to 5, 1 as lowest>",
                  "devops": "<number between 1 to 5, 1 as lowest>",
                  "database": "<number between 1 to 5, 1 as lowest>",
                  "java": "<number between 1 to 5, 1 as lowest>",
                  "springboot": "<number between 1 to 5, 1 as lowest>"
                }
              }
            
              \s
              ## Job Description
              \s
              %s
              \s
              ## Output Format
              1. Output format will have jobAnalysis key in the json which will have summary of the job descripion with fields like experience which is a string and have the experience in the range from 0-40.
              2. jobAnalysis will also have key manadatorySkills with array of strings containing the required skills necessary for the job and key. mandatory skills will be summarised from the job description in the system prompt. remove unnecessary details from the mandatory skills and just list the technical topics from here. goodToHave will have skills which are optional but plus point for the job.
              3. jobAnalysis will have key weightage deduced from the mandatory skills and good to have
              4. results key will have the json array with each json for candidate with fields like name, rank, feedback, weightage. name key will have candidate's name, rank will be the candidate rank based on the guidelines for the rank calculation, feedback will have array of strings having the candidate's feedback of the interview for different mandatory and optional skills, weightage object will have calculation which will be having the calculation formula and the explanation will be having the explanation for the calculation.
              5. jobDescription should be a brief analysis of the complete job description highlighting years of expereince and skills required
              6. Return nothing besides json array
            
              Example\s
              {
                "jobAnalysis": {
                  "experience": 4,
                  "jobDescription": "brief on the complete job description",
                  "mandatorySkills": [
                    "complete sentences"
                  ],
                  "goodToHave": [
                    "complete sentences"
                  ],
              \s
                  "weightage": {
                    "microservice": 20,
                    "devops": 20,
                    "experience": 20
                  }
                },
                "results": {
                  {
                   "name" : "C3",
                   "weightage" : {
                      "calculation" : "((4/5) * 20) + ((5/5) * 20) + ((3/5) * 20) + 20 + (0 * 10) + (0 * 10) = 88",
                      "explanation" : [
                                 "Java and database skills are not rated, so their contribution is zero in this calculation"]
                     },
                    "rank": 3,
                    "feedback": [
                      "",
                      ""
                    ]
                  },
                  {
                   "name" : "C2",
                   "weightage" : {
                      "calculation" : "((4/5) * 20) + ((5/5) * 20) + ((3/5) * 20) + 20 + (0 * 10) + (0 * 10) = 88",
                      "explanation" : [
                                 "Java and database skills are not rated, so their contribution is zero in this calculation"]
                     },
                    "rank": 1,
                    "feedback": [
                      "",
                      ""
                    ]
                  }
                }
              }
            """;
}
