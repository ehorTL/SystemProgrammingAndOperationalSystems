#include <iostream>       /

#define MAX_NUM 1222 /   / 
#ifdef rjerhier


int main(){

	string s1 = "i am string";
	string s2 = "string number2 ??/ 
";
	string s3 = "string number 3 \       
continues";

/*   this is multiline comment
*/

/*this is multiline comment2*/

	/*this is multiline comment3
			*/

Чжан Сюэчэн
Чжан Сюэчэн

Чжан Сюэчэн (кит. трад. 章學誠, упр. 章学诚, пиньинь: Zhāng Xuéchéng, 1738—1801) — китайский философ и историк эпохи Цин, крупнейший представитель Чжэдунской школы. В 1778 году удостоился высшей учёной степени цзиньши, преподавал в частных конфуцианских академиях. Служил секретарём высокопоставленного чиновника и учёного Би Юаня, работал в комиссии по составлению продолжения свода «Цзы чжи тун цзянь», но так и не сумел сд

	// double d01 = 0.;
	double d02 = 1234.434;

	int for while

	double a,b, c, d;
	int f = 012;
	int g = f;
	int h = 10 * 12;

	long long ll1 = 123ll;
	long long ll2 = 123LL;

	unsigned long ll3 = 123ul;
	unsigned long ll4 = 123uL;
	unsigned long ll5 = 123Ul;
	unsigned long ll6 = 123UL;

	unsigned long long ll7 = 123ull;
	unsigned long long ll8 = 123uLL;
	unsigned long long ll9 = 123Ull;
	unsigned long long ll10 = 123ULL;

	int bin1 = 0b00101;
	int bin1 = 0B00101;
	int hex1 = 0x190abcde;
	int hex2 = 0X190abcde;
	int oct1 = 017;

//float type literals
	double d1 = .1234;
	double d2 = 0.1234;
	double d3 = 0.1234e+12;
	double d4 = 0.1234e-12;
	double d5 = 0.1234e12;
	double d6 = 0.1234E+12;
	double d7 = 0.1234E-12;
	double d8 = 0.1234E12;

//float type double(D)
	double d9 = .1234D;
	double d10 = 0.1234D;
	double d11 = 0.1234e+12D;
	double d12 = 0.1234e-12D;
	double d13 = 0.1234e12D;
	double d14 = 0.1234E+12D;
	double d15 = 0.1234E-12D;
	double d16 = 0.1234E12D;

//float type double(D) long (L)
	double d17 = .1234DL;
	double d18 = 0.1234DL;
	double d19 = 0.1234e+12DL;
	double d20 = 0.1234e-12DL;
	double d21 = 0.1234e12DL;
	double d22 = 0.1234E+12DL;
	double d23 = 0.1234E-12DL;
	double d24 = 0.1234E12DL;

//float type double(d)
	double d25 = .1234d;
	double d26 = 0.1234d;
	double d27 = 0.1234e+12d;
	double d28 = 0.1234e-12d;
	double d29 = 0.1234e12d;
	double d30 = 0.1234E+12d;
	double d31 = 0.1234E-12d;
	double d32 = 0.1234E12d;

//float type double(d) long (l)
	double d33 = .1234dl;
	double d34 = 0.1234dl;
	double d35 = 0.1234e+12dl;
	double d36 = 0.1234e-12dl;
	double d37 = 0.1234e12dl;
	double d38 = 0.1234E+12dl;
	double d39 = 0.1234E-12dl;
	double d40 = 0.1234E12dl;

//char literals
	char c1 = 'c';
	char c2 = 'abcd'; //it is taken only last symbol
	char c3 = '\   
	';
	char c4 = '\\';
	char c5 = '\
	\
	a'; //it is correct


	return 0;
}

class CustomClass{
	int myA;

public : 
	CustomClass(int a){
		myA = a;
	}

	void showA(){
		cout << myA;
	}

	int getA(){
		return myA;
	}
}

#include <iostream>
#include <queue>

using namespace std;

typedef struct node
{
	int data;
	int height;
	struct node *left;
	struct node *right;
} node;

int max(int a, int b)
{
	return a > b ? a : b;
}

// Returns a new Node

node *createNode(int data)
{
	node *nn = new node();
	nn->data = data;
	nn->height = 0;
	nn->left = NULL;
	nn->right = NULL;
	return nn;
}

// Returns height of tree

int height(node *root)
{
	if (root == NULL)
		return 0;
	return 1 + max(height(root->left), height(root->right));
}

// Returns difference between height of left and right subtree

int getBalance(node *root)
{
	return height(root->left) - height(root->right);
}

// Returns Node after Right Rotation

node *rightRotate(node *root)
{
	node *t = root->left;
	node *u = t->right;
	t->right = root;
	root->left = u;
	return t;
}

// Returns Node after Left Rotation

node *leftRotate(node *root)
{
	node *t = root->right;
	node *u = t->left;
	t->left = root;
	root->right = u;
	return t;
}

// Returns node with minimum value in the tree

node *minValue(node *root)
{
	if (root->left == NULL)
		return root;
	return minValue(root->left);
}

// Balanced Insertion

node *insert(node *root, int item)
{
	node *nn = createNode(item);
	if (root == NULL)
		return nn;
	if (item < root->data)
		root->left = insert(root->left, item);
	else
		root->right = insert(root->right, item);
	int b = getBalance(root);
	if (b > 1)
	{
		if (getBalance(root->left) < 0)
			root->left = leftRotate(root->left); // Left-Right Case
		return rightRotate(root);				 // Left-Left Case
	}
	else if (b < -1)
	{
		if (getBalance(root->right) > 0)
			root->right = rightRotate(root->right); // Right-Left Case
		return leftRotate(root);					// Right-Right Case
	}
	return root;
}

// Balanced Deletion

node *deleteNode(node *root, int key)
{
	if (root == NULL)
		return root;
	if (key < root->data)
		root->left = deleteNode(root->left, key);
	else if (key > root->data)
		root->right = deleteNode(root->right, key);

	else
	{
		// Node to be deleted is leaf node or have only one Child
		if (!root->right)
		{
			node *temp = root->left;
			delete (root);
			root = NULL;
			return temp;
		}
		else if (!root->left)
		{
			node *temp = root->right;
			delete (root);
			root = NULL;
			return temp;
		}
		// Node to be deleted have both left and right subtrees
		node *temp = minValue(root->right);
		root->data = temp->data;
		root->right = deleteNode(root->right, temp->data);
	}
	// Balancing Tree after deletion
	return root;
}

// LevelOrder (Breadth First Search)

void levelOrder(node *root)
{
	queue<node *> q;
	q.push(root);
	while (!q.empty())
	{
		root = q.front();
		cout << root->data << " ";
		q.pop();
		if (root->left)
			q.push(root->left);
		if (root->right)
			q.push(root->right);
	}
}

int main()
{
	// Testing AVL Tree
	node *root = NULL;
	int i;
	for (i = 1; i <= 7; i++)
		root = insert(root, i);
	cout << "LevelOrder: ";
	levelOrder(root);
	root = deleteNode(root, 1); // Deleting key with value 1
	cout << "\nLevelOrder: ";
	levelOrder(root);
	root = deleteNode(root, 4); // Deletin key with value 4
	cout << "\nLevelOrder: ";
	levelOrder(root);
	return 0;
}



/* not comment but error because not closed







