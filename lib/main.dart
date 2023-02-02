import 'package:flutter/material.dart';
import 'package:nagp_quiz_app/pages/Welcome/welcome_screen.dart';
import 'package:nagp_quiz_app/pages/choose-quiz/choose_quiz.dart';
import 'package:nagp_quiz_app/pages/profile/profile.dart';
import 'package:nagp_quiz_app/pages/quiz/quiz_screen.dart';

void main() => runApp(MaterialApp(
    initialRoute: '/',
    routes: {
      '/': (context) => WelcomeScreen(),
      //'/quizScreen': (context) => QuizScreen(),
      //'/quizScreen': (context) => QuizScreen(),
      //'/quizScreen': (context) => QuizScreen(),
    }
));