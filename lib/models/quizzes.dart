class Quiz {
  final String name;
  final String imagePath;

  Quiz({required this.name, required this.imagePath});
}

List<Quiz> quizSelectionData = [
  Quiz(name: "Flutter", imagePath: "images/flutter.png"),
  Quiz(name: "Java", imagePath: "images/java.png")
];