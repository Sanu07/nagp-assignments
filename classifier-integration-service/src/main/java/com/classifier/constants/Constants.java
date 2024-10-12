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
            **For Example**
            user1: What is Garbage Collection?
            user2: Garbage collection is process of cleaning java memory.
            user1: how many types are there?
            user2: there are 4 types
            
            **Output**
            user1: What is Garbage Collection?how many types are there?
            user2: Garbage collection is process of cleaning java memory. there are 4 types.
            
            *Provide the final output in the below JSON format. It should provide the interviewer name and interviewee name along with list of questions and answers after sanitizing it based on the above rules.* **NO PREAMBLE**
            {
            	"Interviewer": <name>,
            	"Interviewee": <name>,
            	"conversations": [
            		{
            			"question": <question>
            			"answer": <answer>
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
            	"QnA": [
            		{
            			"question": "<question>",
            			"answer": "<answer>",
            			"tag": "<Interview Topic>",
            			"difficulty": "<difficulty level from 1 to 5, 1 being lowest>"
            		}
            	]
            }
            
            ### Set of RULES
            1. **You should consider the experience of the candidate along with the question difficulty while evaluating**.
            	- *"experience": "<0-3>"* Check whether the answer contains basic understanding of the concept or not. If yes, provide full mark(5)
            
            	- *"experience": "<3.1-8>"* Check whether the answer covers major relevant areas.
            
            	- *"experience": "<8.1-20>"* Check whether the answer covers in-depth knowledge along with scope of practical implementations or any example.
            
            2. Provide a score for each question-answer pair from 1 to 5, where 1 is the lowest and 	5 is the highest. The score should reflect how well the answer meets expectations 		based on the candidate's experience level.
            	- Give full marks (5) if the major areas are covered appropriately for their 		  experience level.
            
            3. If the score is less than 4, provide polite and constructive feedback highlighting only the major missing or incorrect points. The feedback should be tailored to the experience level and politely encourage improvement.
            
            4. Provide the output in the below JSON *Output format* for every set of question and answer in the same order. Please follow below guidelines for the final output
            
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
            			"marksObtained": <mark between 1 to 5.\s
            			**Consider the experience and question difficulty as mentioned in the first point in 'Set of RULES'**>,
            			"feedback": [
            				<list of feedback>
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
            #consider yourself as an interviewer analyst
            
            ## your job is to compare multiple JSON Input responses that contains the
             evaluated results on 5 different topics and rank the candidates based on their performance following the below
             mentioned Guidelines and the job description provided\s
            
             *Topics*\s
             1. microservice\s
             2. devops\s
             3. java\s
             4. springboot\s
             5. database\s
            
             ## Guidelines to rank the candidates\s
             1. consider the overall average score in each topic and also consider interview complexity for evaluation.\s
             2. Read the complete Job description first and analyse the requirements of technologies and experience. Based on your analysis assign weightage to the technologies and also to experience.\s
             3. Now based on your assigned weightage to technologies and experience, evaluate and rank the candidates on their
             experience and overall score obtained for individual technologies.\s
             4. Give an in-depth feedback in LIST format on why the particular candidate is chosen over other candidates. The feedback should be solely based on overall score
             obtained and experience\s
             5. Provide an output in given JSON Output Format\s
            
             ## Input JSON Format { "name": <name of the
             candidate> "interviewComplexity":"<number between 1 to 5, 1 as lowest>", "experience": "<in years>" "overall":
             { "microservice":"<number between 1 to 5, 1 as lowest>", "devops":"<number between 1 to 5, 1 as
             lowest>", "database":"<number between 1 to 5, 1 as lowest>", "java":"<number between 1 to 5, 1 as
             lowest>", "springboot":"<number between 1 to 5, 1 as lowest>" }}\s
            
             ## Job Description\s
             %s
            
             ## Output Format\s
             1. Output format will have jobAnalysis key in the json which will have summary of the job descripion with fields like experience which is a
             string and have the experience in the range from 0-40.
             2. jobAnalysis will also have key manadatorySkills with array of strings containing the required skills necessary for the job and key goodToHave will have skills which are optional
             but plus point for the job.\s
             3. jobAnalysis will have key weightage deduced from the mandator skills and good to have
             4. results key will have the key value pair with keys as candidate names and the value will have integer rank and
             feedback which is array of strings.\s
            
             Example
             {
               "jobAnalysis":{
                  "experience":4,
                  "mandatorySkills":[
                     "complete sentences"
                  ],
                  "goodToHave":[
                     "complete sentences"
                  ],
                  "weightage":{
                     "microservice":20,
                     "devops":20,
                     "experience":20
                  }
               },
               "results":{
                  "C1":{
                     "rank":1,
                     "feedback":[
                        "",
                        ""
                     ]
                  },
                  "C2":{
                     "rank":1,
                     "feedback":[
                        "",
                        ""
                     ]
                  },
                  "C3":{
                     "rank":1,
                     "feedback":[
                        "",
                        ""
                     ]
                  }
               }
            }
            """;
}
