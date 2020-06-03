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
function showRandomGreeting() {
  const greetings =
      ['Hello!', '¡Hola!', 'Ni Hao!', 'Bonjour!', 'Namaste!', 
      'Sawasdee Kha!', 'Konnichiwa!'];
  const greetingContainer = document.getElementById('greeting-container');
  const curr = greetingContainer.innerText;
  const greeting = getRandomItem(curr, greetings);

  greetingContainer.innerText = greeting;
}

function showRandomQuote() {
  const quotes = ['We were on a break!', "Joey doesn't share food", 
    'PIVOT!', "You don’t own a TV? What’s all your furniture pointed at?",
    "They don’t know that we know they know we know.", "He's a transponster!",
    "I’m not so good with the advice. Can I interest you in a sarcastic comment?",
    'Could I BE wearing any more clothes?', "You’ve been BAMBOOZLED!",
    "It's like a cow’s opinion. It just doesn’t matter. It’s moo.",
    'Gum would be perfection.'];
  const quoteContainer = document.getElementById("quote-container");
  let curr = quoteContainer.innerText;
  curr = curr.substring(1, curr.length - 1);
  const quote = getRandomItem(curr, quotes);
  quoteContainer.innerText = '"' + quote + '"';
}

function getRandomItem(curr, arr) {
    let item = arr[Math.floor(Math.random() * arr.length)];
    if (curr === item) {
        let filteredArr = arr.filter(e => e!== curr)
        item = filteredArr[Math.floor(Math.random() * filteredArr.length)];
    }
    return item;
}

function getData() {
    console.log("getData() called");
  fetch('/data').then(response => response.text()).then((data) => {
    console.log(data);
    let dataJson = JSON.parse(data);
    console.log(dataJson);
    let comments = dataJson.comments;
    let maxNum = dataJson.maxNumOfComments;
    // console.log(dataJson);
    // console.log(dataJson[0].name);
    document.getElementById('data-container').innerText = comments;
    document.getElementById('number-of-comments').value = maxNum;
  });
}
