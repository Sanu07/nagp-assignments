package com.classifier.constants;

public interface Constants {

    String PROMPT_SANITIZE_TEXT = """
            # Consider yourself as a text sanitization model
                        
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
                 user2: I've worked on creational design patterns that are concerned with how to create objects. Not sure about the
                             other types of patterns.
                 user1: Anything on Structural and Behavioral design patterns?
                 user2:  yes, Structural design patterns focus on the composition of classes or objects to form larger, more complex
                              structures. And Behavioral design patterns deal with the communication and interaction between objects and
                              classes
                
                 **Output**
                 user1: What is Garbage Collection?how many types are there?
                 user2: Garbage collection is process of cleaning java memory. there are 4 types.
                 user1: What are the different design patterns used in Java?
                 user2:  I've worked on creational design patterns that are concerned with how to create objects. Not sure about the
                             other types of patterns.
                 user1: Anything on Structural and Behavioral design patterns?
                 user2:  yes, Structural design patterns focus on the composition of classes or objects to form larger, more complex
                              structures. And Behavioral design patterns deal with the communication and interaction between objects and
                              classes.
                
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
                        
                 a. *evaluation* contains marks and feedback for every set of question and answer in the same order
            
                 b. *analysis* contains a separate JSON object.
                 - "interviewComplexity" denotes the overall complexity of the interview process.
                 - "overall" is a key-value pair where each interview topic gets an overall score based on the questions asked and their complexity. If no questions were asked on a particular topic, leave it as empty.
                 - "positiveFeedback" indicates a list of major strengths shown by the candidate.
                 - "negativeFeedback" highlights the major areas for improvement.
                        
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
              # Consider Yourself an Interview Analyst
            
              ## Task Overview
              Your task is to compare multiple JSON input responses that contain evaluated results across various technologies. Based on this evaluation, you will rank the candidates in ascending order according to their performance, interview complexity, and experience, following the guidelines and job description provided below.
            
              ## Guidelines for Ranking Candidates
            
              1. **Weightage Allocation**:
                 - Assign 5%% weightage to experience.
                 - Distribute the remaining 95%% between mandatory and good-to-have skills, ensuring that mandatory skills receive a higher weightage than good-to-have skills.
              2. **Job Description Analysis**: Thoroughly analyze the job description to identify required technologies and experience. Assign weightage to each mandatory skill based on its importance as derived from the job description.
              3. **Weighted Average Calculation**: Calculate the weighted average percentage using the assigned weightage for technologies and experience, then rank the candidates according to their overall scores.
              4. **Experience Scoring**:
                 - If the required experience states `X`, a candidate with exactly X years of experience should receive the full 5 points.
                 - For candidates with 2 or 4 years of experience, assign 4 points, and so on.
                 - For experience below `X`, assign a score of 0.
                 - If the required experience states `<X>+`, give the maximum score to candidates with experience ranging from `X` to `X+3`, deducting 1 point for each additional year beyond `X+3`, without allowing negative scores.
              5. **In-depth Feedback**: Provide comprehensive feedback in a list format explaining why a particular candidate was selected over others. This feedback should be based solely on the overall score and the candidate's experience.
              6. **Interview Complexity**: Assess the interview complexity on a scale of 1 to 5, where 1 is the easiest and 5 is the most difficult. If a candidate with lower experience has a better score than another whose experience closely matches the requirements, the candidate with lower experience will receive a higher rank.
              7. **Subtopics**: When evaluating technologies, consider subtopics as part of their parent topics. For example:
                 - Under **Database**, evaluate both SQL and NoSQL.
                 - Under **DevOps**, assess Docker and Kubernetes.
                 - For **Python**, ensure candidates are scored based on their actual performance, rather than simply assigning a maximum score.
              8. **Zero Scores**: Evaluate each required technology in the output. If a candidate scores zero in any required or preferred skill, record that score as 0 for that technology.
              9. **Weightage Assignment**: When assigning weightage, assign 0 for any technology that lacks an available score. Do not assume scores. Ensure that the total weightage sums to 100.
              10. **Candidate Ranking Order**: Candidates should be ranked in ascending order of their rank, from top to bottom.
              11. **Output Format**: Provide the output in the specified JSON format outlined in the *Output Format* section.
              12. **No Assumptions**: Do not make any assumptions on your own. If you lack data, *DO NOT MAKE IT UP*.
            
              ## Input JSON Format
            
              ```json
              [
                {
                  "name": "<name of the candidate>",
                  "interviewComplexity": "<number between 1 to 5, where 1 is the lowest>",
                  "experience": "<in years>",
                  "overall": {
                    "<tech-1>": "<number between 1 to 5, where 1 is the lowest>",
                    "<tech-2>": "<number between 1 to 5, where 1 is the lowest>"
                  }
                }
              ]
            ```
              ## Job Description
              %s
          
              ## Output Format
              1. The output will include a jobAnalysis key in the JSON, summarizing the job description with fields like experience (a string representing the experience range from 0-40).
              2. The jobAnalysis will also include mandatorySkills (an array of strings with the necessary skills for the job) and goodToHave (skills that are optional but beneficial).
              3. The jobAnalysis will include a weightage key derived from the mandatory and good-to-have skills.
              4. The results key will contain a JSON array for each candidate, with fields like name, rank, feedback, and weightage. The name key will include the candidate's name, while rank will indicate their ranking based on the guidelines. The feedback will provide detailed reasoning for the candidate's ranking based on overall score and experience. The weightage object will contain the calculation formula and an explanation for the calculation.
              5. The jobDescription should offer a brief analysis of the complete job description, emphasizing the required years of experience and skills.
              6. Return only the JSON array.
          
              ## Example
              ```json
              {
                 "jobAnalysis": {
                   "experience": "<experience required. If it requires 3+, then output as 3+>",
                   "jobDescription": "Brief overview of the complete job description.",
                   "mandatorySkills": [
                     "<List of mandatory skills extracted from the job description>"
                   ],
                   "goodToHave": [
                     "<List of optional skills extracted from the job description>"
                   ],
                   "weightage": {
                     "<technology-1>": "<weightage value>",
                     "<technology-2>": "<weightage value>",
                     "<required experience>": "<weightage value>"
                   }
                 },
                 "results": [
                   {
                     "name": "C3",
                     "weightage": {
                       "calculation": "<Calculate the weighted average percentage, showing the calculation in one line with the final score>",
                       "explanation": [
                         "Details on how the weighted average percentage for this candidate was calculated in 2 lines.",
                         "- 1st line should show the detailed calculation.",
                         "- 2nd line should provide a brief explanation."
                       ]
                     },
                     "rank": 1,
                     "feedback": [
                       "<Detailed explanation in **LIST FORMAT** of why this candidate was chosen for rank 1, and so on>"
                     ]
                   }
                 ]
               }
              ```
            """;
}
