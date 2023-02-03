import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:nagp_quiz_app/constants/constants.dart';
import 'package:nagp_quiz_app/controllers/question_controller.dart';
import 'package:flutter_svg/svg.dart';

class ScoreScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    QuestionController _qnController = Get.put(QuestionController());
    return Scaffold(
      body: Stack(
        fit: StackFit.expand,
        children: [
          SvgPicture.asset("assets/icons/bg.svg", fit: BoxFit.fill),
          Column(
            children: [
              Spacer(flex: 3),
              Text(
                "Score",
                style: Theme.of(context)
                    .textTheme
                    .headline3
                    ?.copyWith(color: Colors.white70),
              ),
              Spacer(),
              Text(
                "${(_qnController.correctAns * 1) - (_qnController.questions.length - _qnController.correctAns) * 0.25}",
                style: Theme.of(context)
                    .textTheme
                    .headline4
                    ?.copyWith(color: Colors.white70),
              ),
              Spacer(flex: 3),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 4.0, horizontal: 4.0),
                child: Card(
                  child: ListTile(
                    title: Text('${_qnController.correctAns}'),
                    leading: CircleAvatar(
                      backgroundImage: AssetImage('assets/images/correct.png'),
                    ),
                  ),
                ),
              ),
              Padding(
                padding: const EdgeInsets.symmetric(vertical: 4.0, horizontal: 4.0),
                child: Card(
                  child: ListTile(
                    title: Text('${_qnController.questions.length - _qnController.correctAns}'),
                    leading: CircleAvatar(
                      backgroundImage: AssetImage('assets/images/incorrect.png'),
                    ),
                  ),
                ),
              ),
            ],
          )
        ],
      ),
    );
  }
}
