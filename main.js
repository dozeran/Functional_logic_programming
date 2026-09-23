// створюємо два порожні масиви
let array1 = [];
let array2 = [];

// генеруємо рандомну довжину масивів
let length1 = Math.floor(Math.random() * 5) + 1;
let length2 = Math.floor(Math.random() * 5) + 1;

// заповнюємо масив1 рандомними цифрами
for (let i = 0; i < length1; i++) {
    array1.push(Math.floor(Math.random() * 9) + 1);
}

// заповнюємо масив2 рандомними цифрами
for (let i = 0; i < length2; i++) {
    array2.push(Math.floor(Math.random() * 9) + 1);
}

// виводимо масиви
console.log("Перший масив:", array1);
console.log("Другий масив:", array2);

// створюємо масив для результату
let result = [];

// починаємо з кінця масивів
let i = array1.length - 1;
let j = array2.length - 1;

// перенос
let carry = 0;

// поки є цифри хоча б в одному масиві або є перенос
while (i >= 0 || j >= 0 || carry > 0) {

    let digit1 = i >= 0 ? array1[i] : 0;
    let digit2 = j >= 0 ? array2[j] : 0;

    // додаємо дві цифри та перенос
    let sum = digit1 + digit2 + carry;

    // цифра, яка залишиться в поточному розряді
    result.unshift(sum % 10);

    // переносимо десяток
    carry = Math.floor(sum / 10);

    i--;
    j--;
}

// виводимо результат
console.log("Результат:", result);