import User from '../model/user.js';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';

const registerUser = async (req, res) => {
    try {
        const { username, password, email, role } = req.body;

        const checkExistingUser = await User.findOne({ $or: [{ username }, { email }] });

        if (checkExistingUser) {
            return res.status(400).json({
                success: false,
                message: "User already exists with provided username or email."
            });
        }

        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(password, salt);

        const newUser = await User.create({
            username,
            email,
            password: hashedPassword,
            role
        });

        if (newUser) {
            res.status(201).json({
                success: true,
                message: "User created successfully"
            });
        } else {
            res.status(400).json({
                success: false,
                message: "An error occured! Please try again."
            });
        }

    } catch (error) {
        console.error("Registration error:", error);
        res.status(500).json({
            success: false,
            message: "An error occured! Please try again."
        });
    }
}

const loginUser = async (req, res) => {
    try {
        const { username, password } = req.body;
        const user = await User.findOne({ username });

        if (!user) {
            return res.status(404).json({
                success: false,
                message: "Invalid username or password."
            });
        }

        const isPasswordValid = await bcrypt.compare(password, user.password);

        if (!isPasswordValid) {
            return res.status(401).json({
                success: false,
                message: "Invalid username or password."
            });
        }

        const accessToken = jwt.sign({
            userId: user._id,
            username: user.username
        }, process.env.JWT_SECRET_KEY, {
            expiresIn: '5m'
        });

        res.status(200).json({
            success: true,
            message: "Login successful",
            accessToken
        });

    } catch (error) {
        res.status(500).json({
            success: false,
            message: "Something went wrong during login. Please try again."
        })
    }
}

export { registerUser, loginUser };