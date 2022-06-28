$("form").on("submit", function(event) {
    event.preventDefault();
    const data = new FormData(event.target);
    const value = Object.fromEntries(data.entries());

    var publicToken = "";

    localStorage.setItem('loginRequest', JSON.stringify(value));

    $.post({
      url: "http://localhost:8080/api/auth/sign-in",
      contentType: 'application/json',
      dataType: 'json',
      data: JSON.stringify(value),
      complete: function(jqXHR) {
          console.log(jqXHR.responseJSON.token);
          var token = jqXHR.responseJSON.token;
          var userId = jqXHR.responseJSON.id;
          publicToken = token;
          console.log(token);
          localStorage.setItem('token', token);
          localStorage.setItem('userId', userId);
          console.log(localStorage.getItem('userId'));
          if (jqXHR.status != 200) {
            alert("Wrong username or password, try again, please");
        }
          if (jqXHR.status == 200) {
              window.location.href = "posts.html";
          }
      },
      error: function() {
        alert("Wrong username or password, try again, please");
      }
    });
})