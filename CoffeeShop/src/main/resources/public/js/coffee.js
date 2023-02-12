let coffee_name = document.querySelector(".coffee_name");
let coffee_filling = document.querySelector(".filling");
let buttons = document.querySelectorAll("button");
let current_element = null;

let changeCoffeeType = (selected) => {
  if (current_element) {
    current_element.classList.remove("selected");
    coffee_filling.classList.remove(current_element.id);
  }
  current_element = selected;
  coffee_filling.classList.add(current_element.id);
  current_element.classList.add("selected");
  coffee_name.innerText = selected.innerText;
};

let setActiveType = (element) => {
  element.toggleClass("selected");
};

[...buttons].forEach((button) => {
  button.addEventListener("click", () => {
    changeCoffeeType(button);
  });
});

