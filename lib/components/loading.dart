import 'package:flutter/material.dart';
import 'package:loading_animations/loading_animations.dart';

class Loading extends StatefulWidget {
  @override
  _LoadingState createState() => _LoadingState();
}

class _LoadingState extends State<Loading> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.blue[900],
        body: Center(
            child: LoadingBouncingGrid.circle(
              borderColor: Colors.cyan,
              borderSize: 3.0,
              size: 50.0,
              backgroundColor: Colors.cyanAccent,
              duration: const Duration(milliseconds: 1000),
            )
        )
    );
  }
}
