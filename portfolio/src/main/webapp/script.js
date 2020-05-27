// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello!', '¡Hola!', 'Ni Hao!', 'Bonjour!', 'Namaste!', 
      'Sawasdee Kha!', 'Konnichiwa!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');

  const curr = greetingContainer.innerText
  if (curr === greeting) {
    greeting = greetings[Math.floor(Math.random() * greetings.length)];
  }

  greetingContainer.innerText = greeting;
}

function addRandomQuote() {
    const quotes = ['We were on a break!', "Joey doesn't share food", 
    'PIVOT!', "You don’t own a TV? What’s all your furniture pointed at?",
    "They don’t know that we know they know we know.", "He's a transponster!",
    "I’m not so good with the advice. Can I interest you in a sarcastic comment?",
    'Could I BE wearing any more clothes?', "You’ve been BAMBOOZLED!",
    "It's like a cow’s opinion. It just doesn’t matter. It’s moo.",
    'Gum would be perfection.'];

    const quote = quotes[Math.floor(Math.random() * quotes.length)];

    const quoteContainer = document.getElementById("quote-container");

    quoteContainer.innerText = '"' + quote + '"';
}
