class Question {
  final int id, answer;
  final String question;
  final List<String> options;

  Question({required this.id, required this.question, required this.answer, required this.options});
}

const List sample_data = [
  {
    "id": 1,
    "question":
    "Java is an open-source UI software development kit created by ______",
    "options": ['Apple', 'Google', 'Facebook', 'Microsoft'],
    "answer_index": 1,
  },
  {
    "id": 2,
    "question": "When Java released Flutter.",
    "options": ['Jun 2017', 'Jun 2017', 'May 2017', 'May 2018'],
    "answer_index": 2,
  },
  {
    "id": 3,
    "question": "How Java types of widgets are there in Flutter?",
    "options": ['1', '3', '2', '4'],
    "answer_index": 2,
  },
  {
    "id": 4,
    "question": "Which Java language is used to build Flutter applications?",
    "options": ['Kotlin', 'Java', 'Dart', 'Go'],
    "answer_index": 2,
  },
  {
    "id": 5,
    "question": "A Java of asynchronous Flutter events is known as a:",
    "options": ['Flow', 'Current', 'Stream', 'Series'],
    "answer_index": 2,
  }
];