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

/**
 * Adds a random greeting to the page from the tv show Friends.
 */
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

/**
 * Returns a random element from the array
 * (one that is different from the one current element).
 */
function getRandomItem(curr, arr) {
    let item = arr[Math.floor(Math.random() * arr.length)];
    if (curr === item) {
        let filteredArr = arr.filter(e => e!== curr)
        item = filteredArr[Math.floor(Math.random() * filteredArr.length)];
    }
    return item;
}

/**
 * Validates that the comment is not empty before making POST request.
 */
function checkForm() {
  const comment = document.getElementById("comments-box").value;
  if (comment === "") {
    return false;
  }
  return true;
}

/**
 * Adds the selected commentsLimit to the localStorage
 * and then reloads the page.
 */
function onChangeSelect() {
    const commentsLimit = document.getElementById("number-of-comments").value;
    window.localStorage.setItem('commentsLimit', commentsLimit);
    getComments();
}

/**
 * Makes a GET request to return the comments based on the 
 * commentsLimit that was in the localStorage.
 */
function getComments() {

    const commentsLimit = window.localStorage.getItem("commentsLimit");

    if (commentsLimit === null) {
        onChangeSelect();
    } else {
      document.getElementById("number-of-comments").value = commentsLimit;

      fetch('/comments?commentsLimit=' + commentsLimit)
        .then(response => response.text()).then((data) => {
        const dataJson = JSON.parse(data);
        const comments = dataJson.comments;

        document.getElementById("comments-container").innerHTML = '';

        comments.forEach(({comment}) => {
          let element = document.createElement("li");
          const text = document.createTextNode(comment);
          element.appendChild(text);
          document.getElementById("comments-container").appendChild(element);
        });

      });
    }
}

/**
 * Makes a POST request to delete all the comments from
 * the datastore and then reloads the page.
 */
function deleteComments() {
    fetch('/delete-comments', {method: 'POST'})
      .then(response => response.text()).then((data) => {
      getComments();
    })
}
