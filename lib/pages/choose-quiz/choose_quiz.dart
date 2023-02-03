import 'package:flutter/material.dart';
import 'package:nagp_quiz_app/models/quizzes.dart';
import 'package:nagp_quiz_app/pages/quiz/quiz_screen.dart';

class ChooseQuiz extends StatefulWidget {
  @override
  _ChooseQuizState createState() => _ChooseQuizState();
}

class _ChooseQuizState extends State<ChooseQuiz> {

  List<Quiz> quizzes = quizSelectionData;

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey[200],
      appBar: AppBar(
        backgroundColor: Colors.blue[900],
        title: Text('Choose a Topic'),
        centerTitle: true,
        elevation: 0,
      ),
      body: ListView.builder(
        itemCount: quizzes.length,
        itemBuilder: (context, index) {
          return Padding(
            padding: const EdgeInsets.symmetric(vertical: 1.0, horizontal: 4.0),
            child: Card(
              child: ListTile(
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: (context) {
                        return QuizScreen();
                      },
                    ),
                  );
                },
                title: Text(quizzes[index].name),
                leading: CircleAvatar(
                  backgroundImage: AssetImage('assets/${quizzes[index].imagePath}'),
                ),
              ),
            ),
          );
        }
      ),
    );
  }
}
