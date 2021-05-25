Итоговый проект.

Реализация многоэтажного здания.
Можно конфигурировать количество этажей, количество лифтов и вместимость лифтов.
На каждом этаже есть две очереди и две кнопки которые включаются если человек становится в соответствующую очередь.

Лифты.
У каждого лифта есть контроллер и массив кнопок этажей и массив людей, реализованный при помощи CopyOnWriteArrayList.
Человек садясь в лифт, добавляется в массив, нажимает на кнопку нужного этажа, контроллер лифта генерирует задание для 
этого лифта, определяет направление движения (сравнивает текущий этаж и этаж назначения), генерирует соответствующее 
задание и добавляет в PriorityBlockingQueue для соответствующего направления. После генерации задания кнопка этажа 
назначения выключается. Так же у лифта есть направление движения, которое меняется контроллером лифта при определенных 
условиях.

Генерация людей.
Скорость генерации (человек за период) зашит в классе PeopleGenerator. После генерации каждый человек становится в 
соответствующую очередь на этаже генерации человека. 

Генерация заданий для лифтов и их распределение.
После генерации людей включаются кнопки на этажах по направлениям очередей людей. Далее запускается генерация заданий MoveTask. 
Сначала по списку этажей фильтруются этажи на которых нажата кнопка "Up" или "Down" и 
Списки заданий передаются контроллерам лифта по приоритету: 1 - Лифт idle = true -> передается
список заданий для лифта который находится к ближайшему этажу с нажатой кнопкой вне зависимости от направления.
Например лифт простаивает на 9 этаже, люди генерируются в очереди вверх на этажах 2,3,4 и на этажах вниз 2,5,8 -> контроллер
лифта получит задачи собрать очереди на этажах 2,5,8 (есть некоторые баги, не успел пофиксить); 2 - Лифт idle = false ->
передаются задания из очередей которые соответствуют направлению движения лифта.

Контроллер лифта.
Выполняет MoveTask из списков в соответствии с направлением движения лифта, безусловно меняет направление движения если 
лифт на первом или последнем этаже. Или при условии что направление в MoveTask не соответствует направлению лифта. 

---------------------
// реализовать сбор статистики (сколько людей перевезено каждым лифтом с каким этажей и на какие этажи) - не выполнено

